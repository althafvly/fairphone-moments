/*
 * Copyright (c) 2025. Fairphone B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fairphone.spring.launcher.util

import android.app.UiModeManager
import android.app.WallpaperManager
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.Intent
import android.content.pm.LauncherApps
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.UserHandle
import android.os.UserManager
import android.util.Log
import androidx.core.net.toUri
import com.fairphone.spring.launcher.data.model.AppInfo
import com.fairphone.spring.launcher.data.repository.AppInfoRepositoryImpl.Companion.TAG

/**
 * Starts the launcher (home) activity.
 */
const val RETAIL_DEMO_APP_PACKAGE_NAME = "com.fairphone.retaildemo2"

fun startLauncherIntent(context: Context) {
    val intent = Intent(Intent.ACTION_MAIN).apply {
        addCategory(Intent.CATEGORY_HOME)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TASK or
                Intent.FLAG_ACTIVITY_NO_ANIMATION
    }
    context.startActivity(intent)
}

/**
 * @return the package name of the default browser app.
 */
fun getDefaultBrowserPackageName(context: Context): String {
    val intent = Intent(Intent.ACTION_VIEW, "https://example.com".toUri())
    intent.addCategory(Intent.CATEGORY_BROWSABLE) // Specify that it's for browsing
    val resolveInfo = context.packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)

    return resolveInfo?.activityInfo?.packageName ?: "com.android.chrome"
}

/**
 * Launches an app.
 */
fun Context.launchApp(app: AppInfo) {
    val primaryUserHandle = android.os.Process.myUserHandle()
    val userHandle = getUserHandleFromId(app.userUuid) ?: primaryUserHandle
    val launcher = getSystemService(Context.LAUNCHER_APPS_SERVICE) as LauncherApps
    val activityInfo = launcher.getActivityList(app.packageName, userHandle)
    val component = if (app.mainActivityClassName.isBlank()) {
        // activityClassName will be null for hidden apps.
        when (activityInfo.size) {
            0 -> {
                //context.showToast(context.getString(R.string.app_not_found))
                return
            }

            1 -> ComponentName(app.packageName, activityInfo[0].name)
            else -> ComponentName(packageName, activityInfo[activityInfo.size - 1].name)
        }
    } else {
        ComponentName(app.packageName, app.mainActivityClassName)
    }

    try {
        launcher.startMainActivity(component, userHandle, null, null)
    } catch (e: SecurityException) {
        Log.e(javaClass.name, e.message, e)
        try {
            launcher.startMainActivity(component, android.os.Process.myUserHandle(), null, null)
        } catch (e: Exception) {
            Log.e(javaClass.name, e.message, e)
            //context.showToast(appContext.getString(R.string.unable_to_open_app))
        }
    } catch (e: Exception) {
        Log.e(javaClass.name, e.message, e)
        //appContext.showToast(appContext.getString(R.string.unable_to_open_app))
    }
}

/**
 * Loads the app infos for the given package names.
 */
fun Context.loadAppInfosByPackageNames(packageNames: List<String>?
): List<AppInfo>  {
    val userManager = getSystemService(Context.USER_SERVICE) as UserManager
    val launcherApps = getSystemService(Context.LAUNCHER_APPS_SERVICE) as LauncherApps
    val density = resources.displayMetrics.densityDpi

    return try {
        // TODO: App appears multiple times if installed in multiple profiles (personal + work)
        // Get all user profiles associated with the current user
        userManager.userProfiles.flatMap { profile -> // Iterate through each profile (UserHandle)
            // Get the list of launchable activities for the specific profile
            val activities = packageNames?.flatMap { packageName ->
                launcherApps.getActivityList(packageName, profile)
            } ?: launcherApps.getActivityList(null, profile)
                .sortedBy { it.label.toString().lowercase() }

            activities.mapNotNull { activityInfo ->
                try {
                    AppInfo(
                        name = activityInfo.label.toString(),
                        mainActivityClassName = activityInfo.componentName.className,
                        packageName = activityInfo.componentName.packageName,
                        icon = activityInfo.getIcon(density),
                        userUuid = activityInfo.user.hashCode()
                    )
                } catch (e: Exception) {
                    Log.w(TAG, "Failed to load info for ${activityInfo.componentName.flattenToString()}", e)
                    null
                }
            }
        }
    } catch (e: SecurityException) {
        Log.e(TAG, "SecurityException accessing LauncherApps. Check permissions or device policy.", e)
        emptyList() // Return empty on permission errors
    } catch (e: Exception) {
        Log.e(TAG, "Error loading apps using LauncherApps", e)
        emptyList() // Return empty list on other errors
    }
}

fun Context.isDarkModeEnabled(): Boolean {
    return uiModeManager().nightMode == UiModeManager.MODE_NIGHT_YES
}

fun Context.isDoNotDisturbAccessGranted(): Boolean {
    return notificationManager().isNotificationPolicyAccessGranted

}

/**
 * Gets the user handle from the user ID.
 */
private fun Context.getUserHandleFromId(userId: Int): UserHandle? {
    val userManager = getSystemService(Context.USER_SERVICE) as UserManager
    val userProfiles = userManager.userProfiles
    return userProfiles.find { it.hashCode() == userId }
}

fun Context.notificationManager() = getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
fun Context.uiModeManager() = getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
fun Context.wallpaperManager() = getSystemService(Context.WALLPAPER_SERVICE) as WallpaperManager

fun Context.hasInternetConnection(): Boolean {
    val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetwork ?: return false
    val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

    return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}

fun Context.isDeviceInRetailDemoMode(): Boolean {
    val dpc = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    return dpc.isDeviceOwnerApp(RETAIL_DEMO_APP_PACKAGE_NAME)
}
