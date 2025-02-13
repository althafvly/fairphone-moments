/*
 * Copyright (c) 2025. Fairphone B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License
 *
 *         at http://www.apache.org/licenses/LICENSE-2.0
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
import com.fairphone.spring.launcher.data.model.Default_App_Packages

interface IAppInfoRepository {
    fun getAllApps(context: Context): List<AppInfo>
    fun getDefaultLauncherApps(context: Context): List<AppInfo>
}

class AppInfoRepository : IAppInfoRepository {

    override fun getAllApps(context: Context): List<AppInfo> {
        val userManager = context.getSystemService(Context.USER_SERVICE) as UserManager
        val launcherApps = context.getSystemService(Context.LAUNCHER_APPS_SERVICE) as LauncherApps

        return try {
            userManager.userProfiles.flatMap { profile ->
                val userId = profile.hashCode()
                // val userId = getIdentifierMethod.invoke(profile) as Int
                launcherApps.getActivityList(null, profile)
                    .filterNot { it.activityInfo.packageName == context.packageName }
                    .mapNotNull {
                        AppInfo(
                            name = it.label.toString(),
                            packageName = it.activityInfo.packageName,
                            mainActivityClassName = it.componentName.className,
                            userUuid = userId,
                        )
                    }
            }
        } catch (e: Exception) {
            Log.e(javaClass.name, "Error loading apps", e)
            emptyList()
        }
    }

    override fun getDefaultLauncherApps(context: Context): List<AppInfo> {
        val allAppsMap = getAllApps(context).associateBy { it.packageName }
        return Default_App_Packages.mapNotNull { allAppsMap[it] }
    }
}
