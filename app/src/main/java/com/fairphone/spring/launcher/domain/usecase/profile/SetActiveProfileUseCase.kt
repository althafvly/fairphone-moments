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

import com.fairphone.spring.launcher.data.model.LauncherProfile
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
