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

import com.fairphone.spring.launcher.data.model.ContactType
import com.fairphone.spring.launcher.data.model.LauncherProfile
import com.fairphone.spring.launcher.data.model.SoundSetting
import com.fairphone.spring.launcher.data.model.UiMode
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
) : UseCase<CreateLauncherProfileUseCase.Params, LauncherProfile>() {

    data class Params(
        val name: String,
        val icon: String,
        val bgColor1: Long,
        val bgColor2: Long,
        val visibleApps: List<String>,
        val allowedContacts: ContactType,
        val customContacts: List<String> = emptyList(),
        val repeatCallEnabled: Boolean,
        val wallpaperId: Int,
        val uiMode: UiMode,
        val blueLightFilterEnabled: Boolean,
        val soundSetting: SoundSetting,
        val batterySaverEnabled: Boolean,
        val reduceBrightnessEnabled: Boolean,
    )

    override suspend fun execute(params: Params): Result<LauncherProfile> {
        val createdZenRuleId = zenNotificationManager.createAutomaticZenRule(
            name = params.name,
            allowedContacts = params.allowedContacts,
            uiMode = params.uiMode,
        )
        val launcherProfile = launcherProfile {
            id = params.name.sanitizeToId()
            name = params.name
            icon = params.icon
            bgColor1 = params.bgColor1
            bgColor2 = params.bgColor2
            visibleApps.addAll(params.visibleApps)
            allowedContacts = params.allowedContacts
            customContacts.addAll(params.customContacts)
            repeatCallEnabled = params.repeatCallEnabled
            wallpaperId = params.wallpaperId
            uiMode = params.uiMode
            blueLightFilterEnabled = params.blueLightFilterEnabled
            soundSetting = params.soundSetting
            batterySaverEnabled = params.batterySaverEnabled
            reduceBrightnessEnabled = params.reduceBrightnessEnabled
            zenRuleId = createdZenRuleId
        }

        launcherProfileRepository.createProfile(profile = launcherProfile)

        return Result.success(launcherProfile)
    }
}