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
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.fairphone.spring.launcher.data.model.Defaults
import com.fairphone.spring.launcher.ui.icons.mode.ModeIcon
import com.fairphone.spring.launcher.ui.screen.mode.creator.ChooseAppsScreen
import com.fairphone.spring.launcher.ui.screen.mode.creator.ChooseBackgroundScreen
import com.fairphone.spring.launcher.ui.screen.mode.creator.CreateModeVisibleAppSelectorViewModel
import com.fairphone.spring.launcher.ui.screen.mode.creator.NameYourMomentScreen
import com.fairphone.spring.launcher.ui.screen.onboarding.OnBoardingViewModel
import com.fairphone.spring.launcher.ui.screen.settings.apps.selector.VisibleAppSelectorScreenState
import com.fairphone.spring.launcher.R
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
data class OnBoardingChooseApps(
    val profileId: String,
    val modeName: String = "",
    val icon: ModeIcon = ModeIcon.Extra6,
    val apps: List<String> = emptyList()
)

/**
 * Used when the user wants to customize his/her background
 */
@Serializable
data class OnBoardingChooseBackground(
    val profileId: String,
    val mainColor: Long,
    val modeName: String = "",
    val icon: ModeIcon = ModeIcon.Extra6,
    val apps: List<String>
)

@Composable
fun OnBoardingNavigation(
    navController: NavHostController,
    onBoardingClose: () -> Unit,
) = NavHost(
    navController = navController,
    startDestination = OnBoardingInit
) {
    composable<OnBoardingInit> {
        val viewModel: OnBoardingViewModel = koinViewModel()
        val screenState by viewModel.screenState.collectAsStateWithLifecycle()

        if (screenState != null) {
            with(screenState!!.activeProfile) {
                NameYourMomentScreen(
                    modeName = name,
                    modeIcon = ModeIcon.valueOf(icon),
                    onContinue = { newName, icon ->
                        val route = OnBoardingChooseApps(
                            profileId = id,
                            modeName = newName,
                            icon = icon,
                            apps = visibleAppsList
                        )
                        navController.navigate(route)
                    }
                )
            }
        }
    }

    composable<OnBoardingChooseApps> { backStackEntry ->
        val args = backStackEntry.toRoute<OnBoardingChooseApps>()

        val viewModel: CreateModeVisibleAppSelectorViewModel = koinViewModel()
        val screenState by viewModel.screenState.collectAsStateWithLifecycle()

        LaunchedEffect(screenState) {
            if (screenState is VisibleAppSelectorScreenState.UpdateAppSelectionSuccess) {
                viewModel.updateSelectProfile(args.profileId, args.apps)
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
                        val route = OnBoardingChooseBackground(
                            apps = data.visibleApps.map { it.packageName },
                            modeName = args.modeName,
                            icon = args.icon,
                            profileId = args.profileId,
                            mainColor = Defaults.Color_BG_Orange
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

    composable<OnBoardingChooseBackground> { backStackEntry ->
        val chooseBackground = backStackEntry.toRoute<OnBoardingChooseBackground>()
        val viewModel: OnBoardingViewModel = koinViewModel()

        ChooseBackgroundScreen(
            selectedColor = chooseBackground.mainColor,
            continueButtonName = stringResource(R.string.bt_apply_changes),
            onContinue = { color1, color2 ->
                viewModel.updateMode(
                    name = chooseBackground.modeName,
                    icon = chooseBackground.icon.name,
                    bgColor1 = color1,
                    bgColor2 = color2,
                    visibleApps = chooseBackground.apps
                )
                onBoardingClose()
            }
        )
    }
}
