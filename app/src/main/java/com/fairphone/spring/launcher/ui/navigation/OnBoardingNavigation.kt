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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.fairphone.spring.launcher.R
import com.fairphone.spring.launcher.data.model.Defaults
import com.fairphone.spring.launcher.ui.icons.mode.ModeIcon
import com.fairphone.spring.launcher.ui.screen.mode.creator.ChooseAppsScreen
import com.fairphone.spring.launcher.ui.screen.mode.creator.ChooseBackgroundScreen
import com.fairphone.spring.launcher.ui.screen.mode.creator.NameYourMomentScreen
import com.fairphone.spring.launcher.ui.screen.onboarding.OnBoardingViewModel
import com.fairphone.spring.launcher.ui.screen.onboarding.UpdateModeState
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

/**
 * Use to initialize the onboarding.
 */
@Serializable
object OnBoardingInit

/**
 * Used when the user wants to customize the defaults apps
 */
@Serializable
data object OnBoardingChooseApps

/**
 * Used when the user wants to customize his/her background
 */
@Serializable
data object OnBoardingChooseBackground

@Composable
fun OnBoardingNavigation(
    navController: NavHostController,
    onBoardingClose: () -> Unit,
) {
    val viewModel: OnBoardingViewModel = koinViewModel()

    NavHost(
        navController = navController,
        startDestination = OnBoardingInit
    ) {
        composable<OnBoardingInit> {
            val activeProfile by viewModel.activeProfile.collectAsStateWithLifecycle()

            if (activeProfile != null) {
                NameYourMomentScreen(
                    modeName = activeProfile!!.name,
                    modeIcon = ModeIcon.valueOf(activeProfile!!.icon),
                    onContinue = { newName, icon ->
                        viewModel.updateName(newName)
                        viewModel.updateIcon(icon)

                        navController.navigate(OnBoardingChooseApps)
                    }
                )
            }
        }

        composable<OnBoardingChooseApps> { backStackEntry ->
            val screenState by viewModel.appSelectorScreenState.collectAsStateWithLifecycle()
            val context = LocalContext.current

            LaunchedEffect(Unit) {
                viewModel.loadApps(context)
            }

            ChooseAppsScreen(
                screenState = screenState,
                onAppClick = viewModel::onAppClick,
                onAppDeselected = viewModel::removeVisibleApp,
                onContinue = {
                    viewModel.updateLauncherProfileApps(viewModel.visibleApps)
                    navController.navigate(OnBoardingChooseBackground)
                }
            )
        }

        composable<OnBoardingChooseBackground> { backStackEntry ->
            val updateProfileState by viewModel.updateModeState.collectAsStateWithLifecycle()

            ChooseBackgroundScreen(
                selectedColor = Defaults.Color_BG_Orange,
                continueButtonName = stringResource(R.string.bt_apply_changes),
                onContinue = { colors ->
                    viewModel.updateBackgroundColors(colors)
                    viewModel.updateProfile()
                }
            )

            LaunchedEffect(updateProfileState) {
                if (updateProfileState is UpdateModeState.Success) {
                    onBoardingClose()
                }
            }
        }
    }
}
