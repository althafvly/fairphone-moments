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

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.LauncherApps
import android.content.pm.PackageManager
import android.os.UserHandle
import android.os.UserManager
import android.util.Log
import androidx.core.net.toUri
import com.fairphone.spring.launcher.data.model.AppInfo

/**
 * Starts the launcher (home) activity.
 */
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
 * Gets the user handle from the user ID.
 */
private fun Context.getUserHandleFromId(userId: Int): UserHandle? {
    val userManager = getSystemService(Context.USER_SERVICE) as UserManager
    val userProfiles = userManager.userProfiles
    return userProfiles.find { it.hashCode() == userId }
}

fun Context.notificationManager() = getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager