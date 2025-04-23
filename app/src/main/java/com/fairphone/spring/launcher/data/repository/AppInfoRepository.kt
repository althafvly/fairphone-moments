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

    override suspend fun getInstalledAppsLauncherApps(context: Context): List<AppInfo> =
        getInstalledAppsLauncherApps(context, null)


    override suspend fun getAppInfo(context: Context, packageName: String): AppInfo? {
        return getInstalledAppsLauncherApps(context, listOf(packageName)).firstOrNull()
    }

    override suspend fun getAppInfos(context: Context, packageNames: List<String>): List<AppInfo> {
        return getInstalledAppsLauncherApps(context, packageNames)
    }

    private suspend fun getInstalledAppsLauncherApps(
        context: Context,
        packageNames: List<String>?
    ): List<AppInfo> = withContext(Dispatchers.IO) {
        val userManager = context.getSystemService(Context.USER_SERVICE) as UserManager
        val launcherApps = context.getSystemService(Context.LAUNCHER_APPS_SERVICE) as LauncherApps
        val density = context.resources.displayMetrics.densityDpi

        try {
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

    companion object {
        const val TAG = "AppInfoRepository"
    }
}
