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

import com.fairphone.spring.launcher.data.model.CreateLauncherProfile
import com.fairphone.spring.launcher.data.model.LauncherProfile
import com.fairphone.spring.launcher.data.model.launcherProfile
import com.fairphone.spring.launcher.data.repository.LauncherProfileRepository
import com.fairphone.spring.launcher.domain.usecase.base.UseCase
import com.fairphone.spring.launcher.util.ZenNotificationManager
import com.fairphone.spring.launcher.util.sanitizeToId

/**
 * Use case to create a new launcher profile.
 */
class CreateLauncherProfileUseCase(
    private val launcherProfileRepository: LauncherProfileRepository,
    private val zenNotificationManager: ZenNotificationManager,
) : UseCase<CreateLauncherProfile, LauncherProfile>() {

    override suspend fun execute(createLauncherProfile: CreateLauncherProfile): Result<LauncherProfile> {
        val createdZenRuleId = zenNotificationManager.createAutomaticZenRule(createLauncherProfile)
        val launcherProfile = launcherProfile {
            id = createLauncherProfile.name.sanitizeToId()
            name = createLauncherProfile.name
            icon = createLauncherProfile.icon
            bgColor1 = createLauncherProfile.bgColor1
            bgColor2 = createLauncherProfile.bgColor2
            visibleApps.addAll(createLauncherProfile.visibleApps)
            allowedContacts = createLauncherProfile.allowedContacts
            customContacts.addAll(createLauncherProfile.customContacts)
            repeatCallEnabled = createLauncherProfile.repeatCallEnabled
            wallpaperId = createLauncherProfile.wallpaperId
            uiMode = createLauncherProfile.uiMode
            blueLightFilterEnabled = createLauncherProfile.blueLightFilterEnabled
            soundSetting = createLauncherProfile.soundSetting
            batterySaverEnabled = createLauncherProfile.batterySaverEnabled
            reduceBrightnessEnabled = createLauncherProfile.reduceBrightnessEnabled
            zenRuleId = createdZenRuleId
        }

        launcherProfileRepository.createProfile(profile = launcherProfile)

        return Result.success(launcherProfile)
    }
}