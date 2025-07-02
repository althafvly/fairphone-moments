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

import android.util.Log
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

    override suspend fun execute(params: LauncherProfile): Result<LauncherProfile> {
        if (launcherProfileRepository.getProfiles().first().size == 1) {
            return Result.failure(IllegalStateException("Cannot remove last profile"))
        }

        val activeProfile = launcherProfileRepository.getActiveProfile().first()

        if (activeProfile.id == params.id) {
            val profiles = launcherProfileRepository.getProfiles().first().filterNot { it.id == params.id }
            launcherProfileRepository.setActiveProfile(profiles.first().id)
        }

        return try {
            val result = zenNotificationManager.removeAutomaticZenRule(params.zenRuleId)

            if (result.isFailure) {
                Result.failure(result.exceptionOrNull()!!)
            } else {
                launcherProfileRepository.deleteProfile(params)
                Result.success(params)
            }
        } catch (e: IllegalStateException) {
            Log.e("DeleteLauncherProfile", "Failed to delete profile", e)
            Result.failure(e)
        }

    }
}
