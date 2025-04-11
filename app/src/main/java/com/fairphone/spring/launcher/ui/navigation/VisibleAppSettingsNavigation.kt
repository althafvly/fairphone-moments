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
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.fairphone.spring.launcher.ui.screen.settings.apps.VisibleAppSelectorScreen
import com.fairphone.spring.launcher.ui.screen.settings.apps.VisibleAppSettingsScreen
import kotlinx.serialization.Serializable

@Serializable
object VisibleAppSettings

@Serializable
object VisibleAppSelector

fun NavGraphBuilder.visibleAppSettingsGraph(navController: NavHostController) {
    // Visible App Settings Screen
    composable<VisibleAppSettings>(
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
        VisibleAppSettingsScreen()
    }

    // Visible App Selector Screen
    composable<VisibleAppSelector>(
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
        VisibleAppSelectorScreen(
            onConfirmAppSelection = {
                navController.navigateUp()
            }
        )
    }
}