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

import com.fairphone.spring.launcher.activity.viewmodel.SwitchStateChangeViewModel
import com.fairphone.spring.launcher.ui.screen.home.HomeScreenViewModel
import com.fairphone.spring.launcher.ui.screen.mode.creator.CreateModeViewModel
import com.fairphone.spring.launcher.ui.screen.mode.switcher.ModeSwitcherViewModel
import com.fairphone.spring.launcher.ui.screen.onboarding.OnBoardingStatusViewModel
import com.fairphone.spring.launcher.ui.screen.onboarding.OnBoardingViewModel
import com.fairphone.spring.launcher.ui.screen.settings.apps.VisibleAppSettingsViewModel
import com.fairphone.spring.launcher.ui.screen.settings.apps.selector.VisibleAppSelectorViewModel
import com.fairphone.spring.launcher.ui.screen.settings.contacts.AllowedContactSettingsViewModel
import com.fairphone.spring.launcher.ui.screen.settings.main.ProfileSettingsViewModel
import com.fairphone.spring.launcher.ui.screen.settings.notifications.AllowedNotificationsAppsViewModel
import com.fairphone.spring.launcher.ui.screen.settings.notifications.AllowedNotificationsSettingsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val uiModule = module {
    viewModelOf(::HomeScreenViewModel)
    viewModelOf(::ModeSwitcherViewModel)
    viewModelOf(::ProfileSettingsViewModel)
    viewModelOf(::VisibleAppSettingsViewModel)
    viewModelOf(::VisibleAppSelectorViewModel)
    viewModelOf(::AllowedContactSettingsViewModel)
    viewModelOf(::AllowedNotificationsSettingsViewModel)
    viewModelOf(::AllowedNotificationsAppsViewModel)
    viewModelOf(::SwitchStateChangeViewModel)
    viewModelOf(::CreateModeViewModel)
    viewModelOf(::OnBoardingViewModel)
    viewModelOf(::OnBoardingStatusViewModel)
}
