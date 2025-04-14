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

package com.fairphone.spring.launcher.ui.screen.settings.apps

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fairphone.spring.launcher.data.model.AppInfo
import com.fairphone.spring.launcher.data.model.LAUNCHER_MAX_APP_COUNT
import com.fairphone.spring.launcher.data.repository.IAppInfoRepository
import com.fairphone.spring.launcher.data.repository.IMomentRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VisibleAppSettingsViewModel(
    context: Context,
    appInfoRepository: IAppInfoRepository,
    private val momentRepository: IMomentRepository,
) : ViewModel() {

    private val _screenState = MutableStateFlow(VisibleAppSelectorScreenState())
    val screenState = _screenState.asStateFlow()

    val currentMoment = momentRepository.getCurrentMoment()

    private var appList = mutableStateListOf<AppInfo>()
    private var visibleApps = mutableStateListOf<AppInfo>()

    init {
        viewModelScope.launch {
            appList.addAll(appInfoRepository.getInstalledAppsLauncherApps(context))
            visibleApps.addAll(appInfoRepository.getAppInfos(context,  currentMoment.visibleApps))

            _screenState.update {
                _screenState.value.copy(
                    appList = appList,
                    visibleApps = visibleApps,
                    showConfirmButton = false,
                    showAppCounter = visibleApps.size == LAUNCHER_MAX_APP_COUNT,
                )
            }
        }
    }

    fun onAppClick(appInfo: AppInfo) {
        if (appInfo in visibleApps) {
            removeVisibleApp(appInfo)
            return
        }
        if (visibleApps.size < LAUNCHER_MAX_APP_COUNT) {
            visibleApps.add(appInfo)
            _screenState.update {
                _screenState.value.copy(
                    visibleApps = visibleApps,
                    showConfirmButton = true,
                    showAppCounter = visibleApps.size == LAUNCHER_MAX_APP_COUNT,
                    showEmptyAppSelectedError = false,
                    showMaxAppSelectedError = false,
                )
            }
        } else if (visibleApps.size == LAUNCHER_MAX_APP_COUNT) {
            viewModelScope.launch {
                _screenState.update {
                    _screenState.value.copy(
                        showMaxAppSelectedError = true,
                    )
                }
                delay(3000)
                _screenState.update {
                    _screenState.value.copy(
                        showMaxAppSelectedError = false,
                    )
                }
            }

        }
    }

    fun removeVisibleApp(appInfo: AppInfo) {
        visibleApps.remove(appInfo)
        _screenState.update {
            _screenState.value.copy(
                visibleApps = visibleApps,
                showConfirmButton = visibleApps.isNotEmpty(),
                showAppCounter = false,
                showEmptyAppSelectedError = visibleApps.isEmpty(),
                showMaxAppSelectedError = false,
            )
        }
    }

    fun confirmAppSelection() {
        viewModelScope.launch {
            //momentRepository.updateVisibleApps(currentMoment, visibleApps)
        }
    }
}

data class VisibleAppSelectorScreenState(
    val appList: List<AppInfo> = emptyList(),
    val visibleApps: List<AppInfo> = emptyList(),
    val showConfirmButton: Boolean = false,
    val showAppCounter: Boolean = false,
    val showEmptyAppSelectedError: Boolean = false,
    val showMaxAppSelectedError: Boolean = false,
)