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

package com.fairphone.spring.launcher.ui.screen.mode.creator

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fairphone.spring.launcher.R
import com.fairphone.spring.launcher.data.model.AppInfo
import com.fairphone.spring.launcher.data.model.CUSTOM_PROFILE_ID
import com.fairphone.spring.launcher.data.model.LAUNCHER_MAX_APP_COUNT
import com.fairphone.spring.launcher.data.repository.AppInfoRepository
import com.fairphone.spring.launcher.ui.screen.settings.apps.selector.ScreenData
import com.fairphone.spring.launcher.ui.screen.settings.apps.selector.VisibleAppSelectorScreenState
import com.fairphone.spring.launcher.ui.screen.settings.apps.selector.updateAppSelectorState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * [com.fairphone.spring.launcher.ui.screen.settings.apps.selector.VisibleAppSelectorViewModel] is used to edit an existing profile, but this one is used
 * on the creation wizard when the profile is not yet created
 */
class CreateModeVisibleAppSelectorViewModel(
    private val context: Application,
    private val appInfoRepository: AppInfoRepository,
) : ViewModel() {

    private val _screenState: MutableStateFlow<VisibleAppSelectorScreenState> =
        MutableStateFlow(VisibleAppSelectorScreenState.Loading)
    val screenState = _screenState.asStateFlow()

    private lateinit var screenData: ScreenData

    var filter: String by mutableStateOf("")

    private var installedApps = mutableStateListOf<AppInfo>()
    private var visibleApps = mutableStateListOf<AppInfo>()

    init {
        viewModelScope.launch {
            installedApps.addAll(appInfoRepository.getAllInstalledApps(context))
            // On Init we only initialize the installed apps on the device
            screenData = ScreenData(
                profileId = CUSTOM_PROFILE_ID,
                appList = installedApps,
                visibleApps = visibleApps,
                showConfirmButton = installedApps.isNotEmpty(),
                showAppCounter = visibleApps.size == LAUNCHER_MAX_APP_COUNT,
                showEmptyAppSelectedError = false,
                showMaxAppSelectedError = false,
                confirmButtonTextResource = R.string.bt_continue
            )

            _screenState.update {
                // Elements ae loaded, and the data will be ready when the updateSelectProfile
                // function will be called
                VisibleAppSelectorScreenState.UpdateAppSelectionSuccess
            }
        }
    }

    fun updateSelectProfile(profileId: String, apps: List<String>) {
        visibleApps.addAll(
            apps.mapNotNull { appPackage ->
                installedApps.firstOrNull { it.packageName == appPackage }
            },
        )
        _screenState.updateAppSelectorState {
            screenData.copy(
                visibleApps = visibleApps,
                profileId = profileId
            )
        }
    }

    fun onAppClick(appInfo: AppInfo) {
        if (appInfo in visibleApps) {
            removeVisibleApp(appInfo)
            return
        }
        if (visibleApps.size < LAUNCHER_MAX_APP_COUNT) {
            visibleApps.add(appInfo)
            _screenState.updateAppSelectorState {
                screenData.copy(
                    visibleApps = visibleApps,
                    showConfirmButton = true,
                    showAppCounter = visibleApps.size == LAUNCHER_MAX_APP_COUNT,
                    showEmptyAppSelectedError = false,
                    showMaxAppSelectedError = false,
                )
            }
        } else if (visibleApps.size == LAUNCHER_MAX_APP_COUNT) {
            viewModelScope.launch {
                _screenState.updateAppSelectorState {
                    screenData.copy(showMaxAppSelectedError = true)
                }
                delay(3000)
                _screenState.updateAppSelectorState {
                    screenData.copy(showMaxAppSelectedError = false)
                }
            }

        }
    }

    fun onFilterChanged(filter: String) {
        this.filter = filter
        _screenState.updateAppSelectorState {
            screenData.copy(
                appList = if (filter.isEmpty()) {
                    installedApps
                } else {
                    installedApps.filter { it.name.contains(filter, ignoreCase = true) }
                }
            )
        }
    }

    fun removeVisibleApp(appInfo: AppInfo) {
        visibleApps.remove(appInfo)
        _screenState.updateAppSelectorState {
            screenData.copy(
                visibleApps = visibleApps,
                showConfirmButton = visibleApps.isNotEmpty(),
                showAppCounter = false,
                showEmptyAppSelectedError = visibleApps.isEmpty(),
                showMaxAppSelectedError = false,
            )
        }
    }
}