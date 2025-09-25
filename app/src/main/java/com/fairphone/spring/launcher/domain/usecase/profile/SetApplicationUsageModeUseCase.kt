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
import com.fairphone.spring.launcher.domain.usecase.base.UseCase

/**
 * Update the current application [UsageMode]
 */
class SetApplicationUsageModeUseCase(private val appPrefs: AppPrefs) : UseCase<UsageMode, Unit>() {
    override suspend fun execute(params: UsageMode): Result<Unit> =
        appPrefs.setUsageMode(params).let {
            Result.success(Unit)
        }
}
