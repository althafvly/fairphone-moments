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

import android.content.Context
import com.fairphone.spring.launcher.R
import com.fairphone.spring.launcher.data.model.CreateLauncherProfile
import com.fairphone.spring.launcher.data.model.Defaults
import com.fairphone.spring.launcher.data.model.LauncherColors
import com.fairphone.spring.launcher.data.model.protos.launcherProfileApp
import com.fairphone.spring.launcher.domain.usecase.base.UseCase
import com.fairphone.spring.launcher.util.isDoNotDisturbAccessGranted
import kotlinx.coroutines.flow.first

class InitializeSpringLauncherUseCase(
    private val context: Context,
    private val createLauncherProfileUseCase: CreateLauncherProfileUseCase,
    private val getAllProfilesUseCase: GetAllProfilesUseCase,
) : UseCase<Unit, Unit>() {

    override suspend fun execute(params: Unit): Result<Unit> {
        val profiles = getAllProfilesUseCase.execute(Unit).first()
        if (profiles.isNotEmpty()) {
            return Result.failure(IllegalStateException("App already initialized"))
        }

        if (context.isDoNotDisturbAccessGranted()) {
            val result = createDefaultProfile(context)

            return result
        } else {
            return Result.failure(IllegalStateException("DND permission not granted"))
        }
    }

    private suspend fun createDefaultProfile(context: Context): Result<Unit> {
        val essentials = CreateLauncherProfile(
            id = CreateLauncherProfileUseCase.newId(),
            name = context.getString(R.string.default_profile_name),
            icon = Defaults.DEFAULT_ICON,
            bgColor1 = LauncherColors.Default.leftColor,
            bgColor2 = LauncherColors.Default.rightColor,
            launcherProfileApps = Defaults.DEFAULT_VISIBLE_APPS.map { app ->
                app.allApps.firstNotNullOf {
                    launcherProfileApp {
                        packageName = it.getPackageName(context)
                        isWorkApp = it.isWorkApp
                    }
                }
            },
            allowedContacts = Defaults.DEFAULT_ALLOWED_CONTACTS,
            repeatCallEnabled = Defaults.DEFAULT_REPEAT_CALL_ENABLED,
            wallpaperId = Defaults.DEFAULT_WALLPAPER_ID,
            uiMode = Defaults.DEFAULT_DARK_MODE_SETTING,
            blueLightFilterEnabled = Defaults.DEFAULT_BLUE_LIGHT_FILTER_ENABLED,
            soundSetting = Defaults.DEFAULT_SOUND_SETTING,
            batterySaverEnabled = Defaults.BATTERY_SAVER_ENABLED,
            reduceBrightnessEnabled = Defaults.REDUCE_BRIGHTNESS_ENABLED,
        )
        val result = createLauncherProfileUseCase.execute(essentials)

        return when {
            result.isFailure -> Result.failure(result.exceptionOrNull() ?: Exception())
            else -> Result.success(Unit)
        }
    }
}