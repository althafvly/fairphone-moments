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

package com.fairphone.spring.launcher.activity.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fairphone.spring.launcher.data.model.LauncherProfile
import com.fairphone.spring.launcher.data.model.SwitchState
import com.fairphone.spring.launcher.domain.usecase.EnableDndUseCase
import com.fairphone.spring.launcher.domain.usecase.profile.GetActiveProfileUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SwitchStateChangeViewModel(
    private val enableDndUseCase: EnableDndUseCase,
    private val getActiveProfileUseCase: GetActiveProfileUseCase,
) : ViewModel() {

    val activeProfile: StateFlow<LauncherProfile?> =
        getActiveProfileUseCase.execute(Unit)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    fun handleDnd(switchState: SwitchState) {
        viewModelScope.launch {
            val enableDnd = when (switchState) {
                SwitchState.ENABLED -> true
                SwitchState.DISABLED -> false
            }
            enableDndUseCase.execute(enableDnd)
        }
    }

    fun handleLockscreenWallpaper(context: Context, switchState: SwitchState) {
        val enableDnd = when (switchState) {
            SwitchState.ENABLED -> true
            SwitchState.DISABLED -> false
        }
        // TODO: Enable lockscreen wallpaper switch when fixed
        // LockscreenWallpaperSwitcherWorker.enqueueWallpaperWork(context, enableDnd)
    }
}
