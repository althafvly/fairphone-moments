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

package com.fairphone.spring.launcher.di

import com.fairphone.spring.launcher.domain.usecase.EnableDndUseCase
import com.fairphone.spring.launcher.domain.usecase.profile.CreateLauncherProfileUseCase
import com.fairphone.spring.launcher.domain.usecase.profile.DeleteLauncherProfileUseCase
import com.fairphone.spring.launcher.domain.usecase.profile.GetActiveProfileUseCase
import com.fairphone.spring.launcher.domain.usecase.profile.GetAllProfilesUseCase
import com.fairphone.spring.launcher.domain.usecase.profile.GetApplicationUsageModeUseCase
import com.fairphone.spring.launcher.domain.usecase.profile.GetEditedProfileUseCase
import com.fairphone.spring.launcher.domain.usecase.profile.InitializeSpringLauncherUseCase
import com.fairphone.spring.launcher.domain.usecase.profile.SetActiveProfileUseCase
import com.fairphone.spring.launcher.domain.usecase.profile.SetApplicationUsageModeUseCase
import com.fairphone.spring.launcher.domain.usecase.profile.SetEditedProfileUseCase
import com.fairphone.spring.launcher.domain.usecase.profile.UpdateLauncherProfileUseCase
import com.fairphone.spring.launcher.util.ZenNotificationManager
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule = module {
    factoryOf(::EnableDndUseCase)
    factoryOf(::CreateLauncherProfileUseCase)
    factoryOf(::UpdateLauncherProfileUseCase)
    factoryOf(::GetActiveProfileUseCase)
    factoryOf(::GetAllProfilesUseCase)
    factoryOf(::GetEditedProfileUseCase)
    factoryOf(::SetActiveProfileUseCase)
    factoryOf(::SetEditedProfileUseCase)
    factoryOf(::InitializeSpringLauncherUseCase)
    factoryOf(::DeleteLauncherProfileUseCase)
    factoryOf(::GetApplicationUsageModeUseCase)
    factoryOf(::SetApplicationUsageModeUseCase)

    factoryOf(::ZenNotificationManager)
}
