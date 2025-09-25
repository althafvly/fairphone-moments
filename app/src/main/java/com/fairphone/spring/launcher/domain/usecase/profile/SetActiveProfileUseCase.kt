/*
 * Copyright (C) 2025 FairPhone B.V.
 *
 * SPDX-FileCopyrightText: 2025. FairPhone B.V.
 *
 * SPDX-License-Identifier: EUPL-1.2
 */

package com.fairphone.spring.launcher.domain.usecase.profile

import com.fairphone.spring.launcher.data.model.protos.LauncherProfile
import com.fairphone.spring.launcher.data.repository.LauncherProfileRepository
import com.fairphone.spring.launcher.domain.usecase.base.UseCase
import com.fairphone.spring.launcher.util.ZenNotificationManager
import kotlinx.coroutines.flow.first

class SetActiveProfileUseCase(
    private val launcherProfileRepository: LauncherProfileRepository,
    private val zenNotificationManager: ZenNotificationManager,
) : UseCase<String, LauncherProfile>() {
    override suspend fun execute(params: String): Result<LauncherProfile> {
        return try {
            val currentActiveProfile = launcherProfileRepository.getActiveProfile().first()
            zenNotificationManager.disableDnd(
                zenRuleId = currentActiveProfile.zenRuleId,
                name = currentActiveProfile.name,
            )

            val newActiveProfile = launcherProfileRepository.getProfile(params).first()
            zenNotificationManager.enableDnd(
                zenRuleId = newActiveProfile.zenRuleId,
                name = newActiveProfile.name,
            )
            launcherProfileRepository.setActiveProfile(params)
            return Result.success(newActiveProfile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
