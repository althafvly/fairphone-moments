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

package com.fairphone.spring.launcher.ui.screen.settings.appearance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fairphone.spring.launcher.data.model.LauncherColors
import com.fairphone.spring.launcher.data.model.protos.LauncherProfile
import com.fairphone.spring.launcher.domain.usecase.profile.GetActiveProfileUseCase
import com.fairphone.spring.launcher.domain.usecase.profile.UpdateLauncherProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WallpaperSettingsViewModel(
    private val getActiveProfileUseCase: GetActiveProfileUseCase,
    private val updateLauncherProfileUseCase: UpdateLauncherProfileUseCase
) : ViewModel() {

    val editedProfile: StateFlow<LauncherProfile?> =
        getActiveProfileUseCase.execute(Unit)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    private val _updateWallpaperState: MutableStateFlow<WallpaperSettingScreenState> =
        MutableStateFlow(WallpaperSettingScreenState.Loading)
    val updateWallpaperState = _updateWallpaperState.asStateFlow()


    fun updateProfileColors(colors: LauncherColors) = viewModelScope.launch {
        _updateWallpaperState.update { WallpaperSettingScreenState.Loading }
        val currentActiveProfile = editedProfile.value

        if (currentActiveProfile == null) {
            _updateWallpaperState.update {
                WallpaperSettingScreenState.Error(IllegalStateException("Cannot update: No active profile."))
            }
            return@launch
        }
        val updatedProfile = currentActiveProfile
            .toBuilder()
            .setBgColor1(colors.leftColor)
            .setBgColor2(colors.rightColor)
            .build()
        val result = updateLauncherProfileUseCase.execute(updatedProfile)

        if (result.isFailure) {
            _updateWallpaperState.update { WallpaperSettingScreenState.Error(result.exceptionOrNull()) }
        } else {
            _updateWallpaperState.update { WallpaperSettingScreenState.Success }
        }
    }

}

sealed class WallpaperSettingScreenState {
    data object Loading : WallpaperSettingScreenState()
    data object Success : WallpaperSettingScreenState()
    data class Error(val exception: Throwable?) : WallpaperSettingScreenState()
}

