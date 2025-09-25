/*
 * Copyright (C) 2025 FairPhone B.V.
 *
 * SPDX-FileCopyrightText: 2025. FairPhone B.V.
 *
 * SPDX-License-Identifier: EUPL-1.2
 */

package com.fairphone.spring.launcher.domain.usecase.profile

import com.fairphone.spring.launcher.data.prefs.AppPrefs
import com.fairphone.spring.launcher.data.prefs.UsageMode
import com.fairphone.spring.launcher.domain.usecase.base.FlowUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Return the current application [], ie onboarding complete, on bording or standard
 */
class GetApplicationUsageModeUseCase(private val appPrefs: AppPrefs) :
    FlowUseCase<Unit, UsageMode>() {
    override fun execute(params: Unit): Flow<UsageMode> =
        flow { appPrefs.usageMode() }
}
