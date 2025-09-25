/*
 * Copyright (C) 2025 FairPhone B.V.
 *
 * SPDX-FileCopyrightText: 2025. FairPhone B.V.
 *
 * SPDX-License-Identifier: EUPL-1.2
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

