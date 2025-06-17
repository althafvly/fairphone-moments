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
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.fairphone.spring.launcher.R
import com.fairphone.spring.launcher.ui.screen.mode.creator.ChooseBackgroundScreen
import com.fairphone.spring.launcher.ui.screen.settings.appearance.AppearanceSettingsScreen
import com.fairphone.spring.launcher.ui.screen.settings.appearance.WallpaperSettingScreenState
import com.fairphone.spring.launcher.ui.screen.settings.appearance.WallpaperSettingsViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
object WallpaperSettings

@Serializable
object AppearanceSettings

fun NavGraphBuilder.appearenceSettingsNavGraph(navController: NavHostController) {

    composable<AppearanceSettings> {
        AppearanceSettingsScreen(
            onCustomizeWallpaperClick = {
                navController.navigate(WallpaperSettings)
            }
        )
    }

    composable<WallpaperSettings> {
        val viewModel: WallpaperSettingsViewModel = koinViewModel()
        val state by viewModel.updateWallpaperState.collectAsStateWithLifecycle()
        val activeProfile by viewModel.editedProfile.collectAsStateWithLifecycle()

        LaunchedEffect(state) {
            if (state is WallpaperSettingScreenState.Success) {
                navController.popBackStack()
            }
        }
        activeProfile?.let {
            ChooseBackgroundScreen(
                continueButtonName = stringResource(R.string.update_wallpaper),
                selectedColor = it.bgColor2,
                onBackgroundColorSelected = { colors ->
                    viewModel.updateProfileColors(colors)
                }
            )
        }
    }
}
