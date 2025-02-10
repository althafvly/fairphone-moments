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

package com.fairphone.spring.launcher.ui.screen.home

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.pm.LauncherApps
import android.os.UserHandle
import android.os.UserManager
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.fairphone.spring.launcher.data.model.AppInfo
import com.fairphone.spring.launcher.util.getUserHandleFromId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeScreenViewModel(
    app: Application
) : AndroidViewModel(app) {

    private val _screenState: MutableStateFlow<HomeScreenState> =
        MutableStateFlow(HomeScreenState(emptyList()))
    val screenState: StateFlow<HomeScreenState> = _screenState.asStateFlow()

    init {
        // TODO: Implement Default App loading algorithm
        getAllApps().filter { it.packageName in DefaultApps }.let { homeApps ->
            _screenState.update { it.copy(appList = homeApps) }
        }
    }

    private fun getAllApps(): List<AppInfo> {
        val context = getApplication<Application>()
        val userManager = context.getSystemService(Context.USER_SERVICE) as UserManager
        val launcherApps = context.getSystemService(Context.LAUNCHER_APPS_SERVICE) as LauncherApps

        return try {
            val getIdentifierMethod = UserHandle::class.java.getDeclaredMethod("getIdentifier")

            userManager.userProfiles.flatMap { profile ->
                val userId = profile.hashCode()
                // val userId = getIdentifierMethod.invoke(profile) as Int
                launcherApps.getActivityList(null, profile)
                    .filterNot { it.activityInfo.packageName != context.packageName }
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

    fun onAppClick(appInfo: AppInfo) {
        val primaryUserHandle = android.os.Process.myUserHandle()
        launchApp(
            appInfo.packageName,
            appInfo.mainActivityClassName,
            getApplication<Application>().getUserHandleFromId(appInfo.userUuid) ?: primaryUserHandle
            //UserHandle.getUserHandleForUid(appInfo.userUuid)
        )
    }

    private fun launchApp(packageName: String, activityClassName: String?, userHandle: UserHandle) {
        val context = getApplication<Application>()
        val launcher = context.getSystemService(Context.LAUNCHER_APPS_SERVICE) as LauncherApps
        val activityInfo = launcher.getActivityList(packageName, userHandle)

        val component = if (activityClassName.isNullOrBlank()) {
            // activityClassName will be null for hidden apps.
            when (activityInfo.size) {
                0 -> {
                    //context.showToast(context.getString(R.string.app_not_found))
                    return
                }

                1 -> ComponentName(packageName, activityInfo[0].name)
                else -> ComponentName(packageName, activityInfo[activityInfo.size - 1].name)
            }
        } else {
            ComponentName(packageName, activityClassName)
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

    companion object {
        val DefaultApps = listOf(
            "com.android.phone",
            "com.google.android.apps.messaging",
            "com.android.chrome",
            "com.fp.camera",
            "com.google.android.apps.maps",
        )
        /*val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                HomeScreenViewModel(
                )
            }
        }*/
    }
}

data class HomeScreenState(
    val appList: List<AppInfo>,
)
