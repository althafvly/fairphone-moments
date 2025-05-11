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

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.fairphone.spring.launcher.ui.screen.settings.main.ProfileSettingsScreen
import com.fairphone.spring.launcher.ui.screen.settings.main.ProfileSettingsViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

const val SLIDE_ANIMATION_DURATION_MILLIS = 300

@Serializable
object ProfileSettings


@Composable
fun SettingsNavigation(
    navController: NavHostController,
) = NavHost(
    navController = navController,
    startDestination = ProfileSettings,
    enterTransition = {
        slideIntoContainer(
            AnimatedContentTransitionScope.SlideDirection.Left,
            animationSpec = tween(SLIDE_ANIMATION_DURATION_MILLIS)
        )
    },
    exitTransition = {
        slideOutOfContainer(
            AnimatedContentTransitionScope.SlideDirection.Left,
            animationSpec = tween(SLIDE_ANIMATION_DURATION_MILLIS)
        )
    },
    popEnterTransition = {
        slideIntoContainer(
            AnimatedContentTransitionScope.SlideDirection.Right,
            animationSpec = tween(SLIDE_ANIMATION_DURATION_MILLIS)
        )
    },
    popExitTransition = {
        slideOutOfContainer(
            AnimatedContentTransitionScope.SlideDirection.Right,
            animationSpec = tween(SLIDE_ANIMATION_DURATION_MILLIS)
        )
    }
) {
    // Moment Settings Screen
    composable<ProfileSettings> {
        val viewModel: ProfileSettingsViewModel = koinViewModel()
        val screenState by viewModel.screenState.collectAsStateWithLifecycle()
        val context = LocalContext.current

        ProfileSettingsScreen(
            screenState = screenState,
            onEditProfileName = viewModel::updateProfileName,
            onNavigateToVisibleAppSettings = {
                navController.navigate(VisibleAppSettings)
            },
            onNavigateToAllowedContactSettings = {
                navController.navigate(AllowedContactSettings)
            },
            onNavigateToNotificationSettings = {},
            onNavigateToAppearanceSettings = {},
            onNavigateToSoundAndVibrationSettings = {},
            onNavigateToPowerSavingSettings = {}
        )

        LaunchedEffect(Unit) {
            viewModel.refreshState(context)
        }
    }

    // Visible App Settings
    visibleAppSettingsNavGraph(navController)

    // Allowed Contact Settings
    allowedContactSettingsNavGraph(navController)
}
