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
import com.fairphone.spring.launcher.data.prefs.UsageMode
import com.fairphone.spring.launcher.domain.usecase.profile.SetApplicationUsageModeUseCase
import kotlinx.coroutines.launch

class OnBoardingStatusViewModel(
    private val setApplicationUsageModeUseCase: SetApplicationUsageModeUseCase
) : ViewModel() {

    /**
     * When the user closes the customization flow before finishing it, they are returned
     * to the home screen with the tool tip that informs them where to adjust Moment
     * settings later on.
     */
    fun cancelOnboarding() {
        viewModelScope.launch {
            setApplicationUsageModeUseCase.execute(UsageMode.ON_BOARDING_COMPLETE)
        }
    }

    /**
     * When the user finalizes the customization flow, they are returned
     * to the home screen with the tool tip that informs them where to adjust Moment
     * settings later on.
     */
    fun completeOnboarding() {
        viewModelScope.launch {
            setApplicationUsageModeUseCase.execute(UsageMode.ON_BOARDING_COMPLETE)
        }
    }
}

