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

package com.fairphone.spring.launcher.domain.usecase.profile

import com.fairphone.spring.launcher.data.model.protos.LauncherProfile
import com.fairphone.spring.launcher.data.repository.LauncherProfileRepository
import com.fairphone.spring.launcher.domain.usecase.base.UseCase
import com.fairphone.spring.launcher.util.ZenNotificationManager
import kotlinx.coroutines.flow.first

/**
 * Use case to create a new launcher profile.
 */
class DeleteLauncherProfileUseCase(
    private val launcherProfileRepository: LauncherProfileRepository,
    private val zenNotificationManager: ZenNotificationManager,
) : UseCase<LauncherProfile, LauncherProfile>() {

    override suspend fun execute(profile: LauncherProfile): Result<LauncherProfile> {
        if (launcherProfileRepository.getProfiles().first().size == 1) {
            return Result.failure(IllegalStateException("Cannot remove last profile"))
        }

        val activeProfile = launcherProfileRepository.getActiveProfile().first()

        if (activeProfile.id == profile.id) {
            val profiles = launcherProfileRepository.getProfiles().first().filterNot { it.id == profile.id }
            launcherProfileRepository.setActiveProfile(profiles.first().id)
        }

        val result = zenNotificationManager.removeAutomaticZenRule(profile.zenRuleId)

        if (result.isFailure) {
            return Result.failure(result.exceptionOrNull()!!)
        } else {
            launcherProfileRepository.deleteProfile(profile)
            return Result.success(profile)
        }
    }
}
