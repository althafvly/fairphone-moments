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

package com.fairphone.spring.launcher.ui.screen.settings.apps.selector

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fairphone.spring.launcher.R
import com.fairphone.spring.launcher.data.model.AppInfo
import com.fairphone.spring.launcher.data.model.LAUNCHER_MAX_APP_COUNT
import com.fairphone.spring.launcher.data.repository.AppInfoRepository
import com.fairphone.spring.launcher.domain.usecase.profile.GetEditedProfileUseCase
import com.fairphone.spring.launcher.domain.usecase.profile.UpdateLauncherProfileUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VisibleAppSelectorViewModel(
    context: Application,
    private val appInfoRepository: AppInfoRepository,
    private val getEditedProfileUseCase: GetEditedProfileUseCase,
    private val updateLauncherProfileUseCase: UpdateLauncherProfileUseCase,
) : ViewModel() {

    private val _screenState: MutableStateFlow<VisibleAppSelectorScreenState> =
        MutableStateFlow(VisibleAppSelectorScreenState.Loading)
    val screenState = _screenState.asStateFlow()

    private lateinit var screenData: ScreenData

    private var installedApps = mutableStateListOf<AppInfo>()
    private var visibleApps = mutableStateListOf<AppInfo>()

    init {
        viewModelScope.launch {
            installedApps.addAll(appInfoRepository.getAllInstalledApps(context))
            val currentProfile = getEditedProfileUseCase.execute(Unit).first()
            visibleApps.addAll(
                appInfoRepository.getAppInfosByPackageNames(
                    context,
                    currentProfile.visibleAppsList
                )
            )

            screenData = ScreenData(
                appList = installedApps,
                visibleApps = visibleApps,
                showConfirmButton = false,
                showAppCounter = visibleApps.size == LAUNCHER_MAX_APP_COUNT,
                showEmptyAppSelectedError = false,
                showMaxAppSelectedError = false,
                confirmButtonTextResource = R.string.bt_confirm
            )

            _screenState.update {
                VisibleAppSelectorScreenState.Ready(screenData)
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
                VisibleAppSelectorScreenState.Ready(
                    screenData.copy(
                        visibleApps = visibleApps,
                        showConfirmButton = true,
                        showAppCounter = true,
                        showEmptyAppSelectedError = false,
                        showMaxAppSelectedError = false,
                    )
                )
            }
        } else if (visibleApps.size == LAUNCHER_MAX_APP_COUNT) {
            viewModelScope.launch {
                _screenState.update {
                    VisibleAppSelectorScreenState.Ready(
                        screenData.copy(
                            showMaxAppSelectedError = true,
                        )
                    )
                }
                delay(3000)
                _screenState.update {
                    VisibleAppSelectorScreenState.Ready(
                        screenData.copy(
                            showMaxAppSelectedError = false,
                        )
                    )
                }
            }

        }
    }

    fun removeVisibleApp(appInfo: AppInfo) {
        visibleApps.remove(appInfo)
        _screenState.updateAppSelectorState {
            screenData.copy(
                visibleApps = visibleApps,
                showConfirmButton = visibleApps.isNotEmpty(),
                showAppCounter = true,
                showEmptyAppSelectedError = visibleApps.isEmpty(),
                showMaxAppSelectedError = false,
            )
        }
    }

    fun confirmAppSelection() = viewModelScope.launch {
        val editedProfile = getEditedProfileUseCase.execute(Unit).first()
        val newProfile = editedProfile.toBuilder()
            .clearVisibleApps()
            .addAllVisibleApps(visibleApps.map { it.packageName })
            .build()
        val result = updateLauncherProfileUseCase.execute(newProfile)
        _screenState.update {
            if (result.isSuccess) {
                VisibleAppSelectorScreenState.UpdateAppSelectionSuccess
            } else {
                VisibleAppSelectorScreenState.UpdateAppSelectionFailure
            }
        }
    }
}

fun MutableStateFlow<VisibleAppSelectorScreenState>.updateAppSelectorState(
    screenDataModifier: () -> ScreenData
) {
    update {
        VisibleAppSelectorScreenState.Ready(
            screenDataModifier.invoke()
        )
    }
}

sealed class VisibleAppSelectorScreenState {
    data object Loading : VisibleAppSelectorScreenState()
    data class Ready(val data: ScreenData) : VisibleAppSelectorScreenState()
    data object UpdateAppSelectionSuccess : VisibleAppSelectorScreenState()
    data object UpdateAppSelectionFailure : VisibleAppSelectorScreenState()
}

data class ScreenData(
    val appList: List<AppInfo>,
    val visibleApps: List<AppInfo>,
    val showConfirmButton: Boolean,
    val confirmButtonTextResource: Int,
    val showAppCounter: Boolean,
    val showEmptyAppSelectedError: Boolean,
    val showMaxAppSelectedError: Boolean,
    val maxItemCount: Int = 5,
)
