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

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.fairphone.spring.launcher.activity.LauncherSettingsActivity
import com.fairphone.spring.launcher.data.model.CUSTOM_PROFILE_ID
import com.fairphone.spring.launcher.data.model.CreateLauncherProfile
import com.fairphone.spring.launcher.data.model.Defaults
import com.fairphone.spring.launcher.data.model.Presets
import com.fairphone.spring.launcher.domain.usecase.profile.CreateLauncherProfileUseCase
import com.fairphone.spring.launcher.ui.icons.mode.ModeIcon
import com.fairphone.spring.launcher.ui.screen.mode.add.name.NameYourMomentScreen
import com.fairphone.spring.launcher.ui.screen.mode.creator.ChooseAppsScreen
import com.fairphone.spring.launcher.ui.screen.mode.creator.ChooseBackgroundScreen
import com.fairphone.spring.launcher.ui.screen.mode.creator.CreateModeViewModel
import com.fairphone.spring.launcher.ui.screen.mode.creator.SelectModeScreen
import com.fairphone.spring.launcher.ui.screen.settings.apps.CreateModeVisibleAppSelectorViewModel
import com.fairphone.spring.launcher.ui.screen.settings.apps.VisibleAppSelectorScreenState
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
object SelectMode

@Serializable
data class NameYourMoment(val modeName: String = "", val icon: ModeIcon = ModeIcon.Extra6)

@Serializable
data class ChooseApps(
    val preset: Presets,
    val modeName: String = "",
    val icon: ModeIcon = ModeIcon.Extra6,
    val apps: List<String> = emptyList()
)

@Serializable
data class ChooseBackground(
    val preset: Presets,
    val modeName: String = "",
    val icon: ModeIcon = ModeIcon.Extra6,
    val apps: List<String>
)

fun NavHostController.closeAndReturnToSwitcher() {
    navigate(ModeSwitcher) {
        popUpTo(route = ModeSwitcher) { inclusive = true }
    }
}

fun NavGraphBuilder.createModeNavGraph(navController: NavHostController) {
    // Select Mode Screen
    composable<SelectMode> {
        SelectModeScreen(
            profiles = Presets.entries,
            onNavigateClose = {
                navController.closeAndReturnToSwitcher()
            },
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
                            preset = preset,
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
            onNavigateBack = {
                navController.navigate(SelectMode) {
                    popUpTo(route = SelectMode) { inclusive = true }
                }
            },
            onNavigateClose = {
                navController.closeAndReturnToSwitcher()
            },
            onContinue = { newName ->
                navController.navigate(
                    ChooseApps(
                        preset = Presets.Custom,
                        modeName = newName,
                        icon = nameYourMoment.icon,
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
            if (screenState is VisibleAppSelectorScreenState.Loading) {
                viewModel.updateSelectProfile(chooseApps.preset, chooseApps.apps)
            }
        }

        ChooseAppsScreen(
            screenState = screenState,
            filter = viewModel.filter,
            onAppFilterChanged = viewModel::onFilterChanged,
            onAppClick = viewModel::onAppClick,
            onAppDeselected = viewModel::removeVisibleApp,
            onNavigateBack = {
                if (ModeIcon.customIcons().contains(chooseApps.icon)) {
                    val route = NameYourMoment(chooseApps.modeName, chooseApps.icon)
                    navController.navigate(route) {
                        popUpTo(route = route) { inclusive = true }
                    }
                } else {
                    navController.navigate(SelectMode) {
                        popUpTo(route = SelectMode) { inclusive = true }
                    }
                }
            },
            onNavigateClose = {
                navController.closeAndReturnToSwitcher()
            },
            onContinue = {
                when (viewModel.screenState.value) {
                    is VisibleAppSelectorScreenState.Ready -> {
                        val data =
                            (viewModel.screenState.value as VisibleAppSelectorScreenState.Ready).data
                        val route = ChooseBackground(
                            apps = data.visibleApps.map { it.packageName },
                            modeName = chooseApps.modeName,
                            icon = chooseApps.icon,
                            preset = chooseApps.preset
                        )
                        navController.navigate(route)
                    }

                    else -> {
                        // Not possible here
                    }
                }

            }
        )
    }

    composable<ChooseBackground> { backStackEntry ->
        val chooseBackground = backStackEntry.toRoute<ChooseBackground>()
        val viewModel: CreateModeViewModel = koinViewModel()
        val context = LocalContext.current

        ChooseBackgroundScreen(
            preset = chooseBackground.preset,
            onNavigateClose = {
                navController.closeAndReturnToSwitcher()
            },
            onNavigateBack = {
                val route = ChooseApps(
                    preset = chooseBackground.preset,
                    icon = chooseBackground.icon,
                    modeName = chooseBackground.modeName,
                    apps = chooseBackground.apps
                )
                navController.navigate(route) {
                    popUpTo(route = route) { inclusive = true }
                }
            },
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
                // Before moving to the settings, we're going back on the switcher. So, the user will
                // see this switcher if they close the settings
                navController.navigate(ModeSwitcher) {
                    popUpTo(route = ModeSwitcher) { inclusive = true }
                }
                LauncherSettingsActivity.start(context)
            }
        )
    }
}
