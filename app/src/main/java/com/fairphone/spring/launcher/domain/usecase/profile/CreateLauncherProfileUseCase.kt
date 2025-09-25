/*
 * Copyright (C) 2025 FairPhone B.V.
 *
 * SPDX-FileCopyrightText: 2025. FairPhone B.V.
 *
 * SPDX-License-Identifier: EUPL-1.2
 */

package com.fairphone.spring.launcher.domain.usecase.profile

import com.fairphone.spring.launcher.data.model.CreateLauncherProfile
import com.fairphone.spring.launcher.data.model.protos.LauncherProfile
import com.fairphone.spring.launcher.data.model.protos.launcherProfile
import com.fairphone.spring.launcher.data.repository.LauncherProfileRepository
import com.fairphone.spring.launcher.domain.usecase.base.UseCase
import com.fairphone.spring.launcher.util.ZenNotificationManager
import java.util.UUID

/**
 * Use case to create a new launcher profile.
 */
class CreateLauncherProfileUseCase(
    private val launcherProfileRepository: LauncherProfileRepository,
    private val zenNotificationManager: ZenNotificationManager,
) : UseCase<CreateLauncherProfile, LauncherProfile>() {

    override suspend fun execute(createLauncherProfile: CreateLauncherProfile): Result<LauncherProfile> {
        return try {
            // Create automatic zen rule
            val createdZenRuleId = zenNotificationManager.createAutomaticZenRule(createLauncherProfile)

            // Create launcher profile
            val launcherProfile = launcherProfile {
                id = createLauncherProfile.id
                name = createLauncherProfile.name
                icon = createLauncherProfile.icon
                bgColor1 = createLauncherProfile.bgColor1
                bgColor2 = createLauncherProfile.bgColor2
                launcherProfileApps.addAll(createLauncherProfile.launcherProfileApps)
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

            Result.success(launcherProfile)
        } catch (e: IllegalStateException) {
            Result.failure(e)
        }
    }

    companion object {
        fun newId(): String =
            UUID.randomUUID().toString()
    }
}