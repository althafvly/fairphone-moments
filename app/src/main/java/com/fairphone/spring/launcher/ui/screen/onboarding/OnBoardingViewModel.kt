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

package com.fairphone.spring.launcher.ui.screen.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fairphone.spring.launcher.data.model.LauncherProfile
import com.fairphone.spring.launcher.domain.usecase.profile.GetActiveProfileUseCase
import com.fairphone.spring.launcher.domain.usecase.profile.UpdateLauncherProfileUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class OnBoardingViewModel(
    private val updateLauncherProfileUseCase: UpdateLauncherProfileUseCase,
    private val getActiveProfileUseCase: GetActiveProfileUseCase
) : ViewModel() {

    val screenState: StateFlow<OnBoardingScreenState?> =
        getActiveProfileUseCase.execute(Unit)
            .map {
                OnBoardingScreenState(
                    activeProfile = it
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = null,
            )

    /**
     * Used to update the current mode at the end of the onboarding process
     */
    fun updateMode(
        name: String,
        icon: String,
        bgColor1: Long,
        bgColor2: Long,
        visibleApps: List<String>
    ) {
        viewModelScope.launch {
            val profile = getActiveProfileUseCase.execute(Unit).first()
            val updatedProfile = profile
                .toBuilder()
                .setName(name)
                .setBgColor1(bgColor1)
                .setBgColor2(bgColor2)
                .setIcon(icon)
                .clearVisibleApps()
                .addAllVisibleApps(visibleApps)
                .build()
            updateLauncherProfileUseCase.execute(updatedProfile)
        }
    }
}

data class OnBoardingScreenState(
    val activeProfile: LauncherProfile
)
