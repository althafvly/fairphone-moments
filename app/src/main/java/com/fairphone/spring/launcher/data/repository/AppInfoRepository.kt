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

package com.fairphone.spring.launcher.data.repository

import android.content.Context
import android.content.pm.LauncherApps
import android.os.UserManager
import android.util.Log
import com.fairphone.spring.launcher.data.model.AppInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface IAppInfoRepository {
    suspend fun getInstalledAppsLauncherApps(context: Context): List<AppInfo>
    suspend fun getAppInfo(context: Context, packageName: String): AppInfo?
    suspend fun getAppInfos(context: Context, packageNames: List<String>): List<AppInfo>
}

class AppInfoRepository : IAppInfoRepository {

    override suspend fun getInstalledAppsLauncherApps(context: Context): List<AppInfo> = withContext(Dispatchers.IO) {
        // Get the required system services
        val userManager = context.getSystemService(Context.USER_SERVICE) as UserManager
        val launcherApps = context.getSystemService(Context.LAUNCHER_APPS_SERVICE) as LauncherApps
        // Get screen density to load icons correctly sized
        val density = context.resources.displayMetrics.densityDpi

        try {
            // Get all user profiles associated with the current user
            userManager.userProfiles.flatMap { profile -> // Iterate through each profile (UserHandle)
                // Get the list of launchable activities for the specific profile
                launcherApps.getActivityList(null, profile)
                    // Filter out your own app's package
                    .filterNot { it.componentName.packageName == context.packageName }
                    .mapNotNull { activityInfo ->
                        try {
                            // Correctly load the icon and label for the activity
                            val iconDrawable = activityInfo.getIcon(density)
                            val labelString = activityInfo.label.toString()
                            // You can also get componentName, applicationInfo etc. from activityInfo if needed

                            AppInfo(
                                name = labelString,
                                mainActivityClassName = activityInfo.componentName.className,
                                packageName = activityInfo.componentName.packageName,
                                icon = iconDrawable,
                                userUuid = activityInfo.user.hashCode()
                                // Add other fields if your AppInfo requires them:
                                // mainActivityClassName = activityInfo.componentName.className,
                                // userHandle = profile // Store the UserHandle if you need to act on apps for specific users
                            )
                        } catch (e: Exception) {
                            // Log error if loading info for a specific app fails
                            Log.w("AppList", "Failed to load info for ${activityInfo.componentName.flattenToString()}", e)
                            null // Skip this app if loading fails
                        }
                    }
            }.sortedBy { it.name.lowercase() } // Sort the final combined list alphabetically
        } catch (e: SecurityException) {
            Log.e("AppList", "SecurityException accessing LauncherApps. Check permissions or device policy.", e)
            emptyList() // Return empty on permission errors
        } catch (e: Exception) {
            Log.e("AppList", "Error loading apps using LauncherApps", e)
            emptyList() // Return empty list on other errors
        }
    }

    override suspend fun getAppInfo(context: Context, packageName: String): AppInfo? {
        return getInstalledAppsLauncherApps(context)
            .find { it.packageName == packageName }
    }

    override suspend fun getAppInfos(context: Context, packageNames: List<String>): List<AppInfo> {
        return getInstalledAppsLauncherApps(context)
            .filter { packageNames.contains(it.packageName) }
    }
}
