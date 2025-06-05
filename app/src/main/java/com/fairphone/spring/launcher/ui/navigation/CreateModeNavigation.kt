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

package com.fairphone.spring.launcher.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.fairphone.spring.launcher.data.model.CUSTOM_PROFILE_ID
import com.fairphone.spring.launcher.data.model.CreateLauncherProfile
import com.fairphone.spring.launcher.data.model.Defaults
import com.fairphone.spring.launcher.data.model.Presets
import com.fairphone.spring.launcher.domain.usecase.profile.CreateLauncherProfileUseCase
import com.fairphone.spring.launcher.ui.icons.mode.ModeIcon
import com.fairphone.spring.launcher.ui.screen.mode.creator.ChooseAppsScreen
import com.fairphone.spring.launcher.ui.screen.mode.creator.ChooseBackgroundScreen
import com.fairphone.spring.launcher.ui.screen.mode.creator.CreateModeViewModel
import com.fairphone.spring.launcher.ui.screen.mode.creator.CreateModeVisibleAppSelectorViewModel
import com.fairphone.spring.launcher.ui.screen.mode.creator.NameYourMomentScreen
import com.fairphone.spring.launcher.ui.screen.mode.creator.SelectModeScreen
import com.fairphone.spring.launcher.ui.screen.settings.apps.selector.VisibleAppSelectorScreenState
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
object SelectMode

@Serializable
data class NameYourMoment(
    val modeName: String = "",
    val icon: ModeIcon = ModeIcon.Extra6
)

@Serializable
data class ChooseApps(
    val profileId: String,
    val modeName: String = "",
    val icon: ModeIcon = ModeIcon.Extra6,
    val apps: List<String> = emptyList()
)

@Serializable
data class ChooseBackground(
    val profileId: String,
    val mainColor: Long,
    val modeName: String = "",
    val icon: ModeIcon = ModeIcon.Extra6,
    val apps: List<String>
)

@Composable
fun CreateModeNavigation(
    navController: NavHostController,
    onModeCreated: () -> Unit,
) = NavHost(
    navController = navController,
    startDestination = SelectMode
) {
    // Select Mode Screen
    composable<SelectMode> {
        SelectModeScreen(
            profiles = Presets.presetSelectionForNewMode,
            onModeSettingsClick = { selectedProfile ->
                if (selectedProfile.id == CUSTOM_PROFILE_ID) {
                    navController.navigate(
                        NameYourMoment(
                            modeName = "",
                            // For custom modes the icon is assigned randomly.
                            icon = ModeIcon.customIcons().random()
                        )
                    )
                } else {
                    val preset = Presets.entries.first { it.profile.id == selectedProfile.id }
                    navController.navigate(
                        ChooseApps(
                            profileId = preset.profile.id,
                            modeName = selectedProfile.name,
                            icon = ModeIcon.valueOf(selectedProfile.icon),
                            apps = preset.profile.visibleAppsList
                        )
                    )
                }
            }
        )
    }

    composable<NameYourMoment> { backStackEntry ->
        val nameYourMoment = backStackEntry.toRoute<NameYourMoment>()
        NameYourMomentScreen(
            modeName = nameYourMoment.modeName,
            modeIcon = nameYourMoment.icon,
            onContinue = { newName, newIcon ->
                navController.navigate(
                    ChooseApps(
                        profileId = Presets.Custom.profile.id,
                        modeName = newName,
                        icon = newIcon,
                        apps = emptyList()
                    )
                )
            }
        )
    }

    composable<ChooseApps> { backStackEntry ->
        val chooseApps = backStackEntry.toRoute<ChooseApps>()

        val viewModel: CreateModeVisibleAppSelectorViewModel = koinViewModel()
        val screenState by viewModel.screenState.collectAsStateWithLifecycle()

        LaunchedEffect(screenState) {
            if (screenState is VisibleAppSelectorScreenState.UpdateAppSelectionSuccess) {
                viewModel.updateSelectProfile(chooseApps.profileId, chooseApps.apps)
            }
        }

        ChooseAppsScreen(
            screenState = screenState,
            filter = viewModel.filter,
            onAppFilterChanged = viewModel::onFilterChanged,
            onAppClick = viewModel::onAppClick,
            onAppDeselected = viewModel::removeVisibleApp,
            onContinue = {
                when (viewModel.screenState.value) {
                    is VisibleAppSelectorScreenState.Ready -> {
                        val data =
                            (viewModel.screenState.value as VisibleAppSelectorScreenState.Ready).data
                        val route = ChooseBackground(
                            apps = data.visibleApps.map { it.packageName },
                            modeName = chooseApps.modeName,
                            icon = chooseApps.icon,
                            profileId = chooseApps.profileId,
                            mainColor = Presets.entries.first { it.profile.id == chooseApps.profileId }.profile.bgColor2
                        )
                        navController.navigate(route)
                    }

                    else -> {
                        // Ignore
                    }
                }

            }
        )
    }

    composable<ChooseBackground> { backStackEntry ->
        val chooseBackground = backStackEntry.toRoute<ChooseBackground>()
        val viewModel: CreateModeViewModel = koinViewModel()

        ChooseBackgroundScreen(
            selectedColor = chooseBackground.mainColor,
            onContinue = { color1, color2 ->
                val params = CreateLauncherProfile(
                    id = CreateLauncherProfileUseCase.newId(),
                    name = chooseBackground.modeName,
                    icon = chooseBackground.icon.name,
                    bgColor1 = color1,
                    bgColor2 = color2,
                    visibleApps = chooseBackground.apps,
                    allowedContacts = Defaults.DEFAULT_ALLOWED_CONTACTS,
                    repeatCallEnabled = Defaults.DEFAULT_REPEAT_CALL_ENABLED,
                    wallpaperId = Defaults.DEFAULT_WALLPAPER_ID,
                    uiMode = Defaults.DEFAULT_DARK_MODE_SETTING,
                    blueLightFilterEnabled = Defaults.DEFAULT_BLUE_LIGHT_FILTER_ENABLED,
                    soundSetting = Defaults.DEFAULT_SOUND_SETTING,
                    batterySaverEnabled = Defaults.BATTERY_SAVER_ENABLED,
                    reduceBrightnessEnabled = Defaults.REDUCE_BRIGHTNESS_ENABLED,
                )
                viewModel.save(params)
                onModeCreated()
            }
        )
    }
}
