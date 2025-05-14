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

import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fairphone.spring.launcher.activity.LauncherSettingsActivity
import com.fairphone.spring.launcher.data.model.colors
import com.fairphone.spring.launcher.ui.component.AnimatedBackground
import com.fairphone.spring.launcher.ui.screen.home.HomeScreen
import com.fairphone.spring.launcher.ui.screen.home.HomeScreenViewModel
import com.fairphone.spring.launcher.ui.screen.mode.ModeSwitcherScreen
import com.fairphone.spring.launcher.ui.screen.mode.ModeSwitcherViewModel
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
object Home

@Serializable
object ModeSwitcher

@Composable
fun HomeNavigation(
    navController: NavHostController = rememberNavController(),
) = NavHost(
    navController = navController,
    startDestination = Home
) {
    composable<Home>(
        enterTransition = {
            expandVertically(
                expandFrom = Alignment.Top,
                animationSpec = tween(
                    durationMillis = 300
                )
            )
        },
        exitTransition = {
            shrinkVertically(
                shrinkTowards = Alignment.Top,
                animationSpec = tween(
                    durationMillis = 300
                )
            )
        },
        popEnterTransition = {
            expandVertically(
                expandFrom = Alignment.Top,
                animationSpec = tween(
                    durationMillis = 300
                )
            )
        },
        popExitTransition = {
            shrinkVertically(
                shrinkTowards = Alignment.Top,
                animationSpec = tween(
                    durationMillis = 300
                )
            )
        }
    ) {
        val viewModel: HomeScreenViewModel = koinViewModel()
        val screenState by viewModel.screenState.collectAsStateWithLifecycle()
        var visibility by remember { mutableStateOf(false) }

        if (screenState != null) {
//            AnimatedVisibility(
//                visible = visibility,
//                enter = expandVertically(
//                    expandFrom = Alignment.Top,
//                    animationSpec = tween(
//                        durationMillis = 200
//                    )
//                ),
//                exit = shrinkVertically(
//                    shrinkTowards = Alignment.Top,
//                    animationSpec = tween(
//                        durationMillis = 200
//                    )
//                ),
//            ) {
                AnimatedBackground(
                    colors = screenState!!.activeProfile.colors(),
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        HomeScreen(
                            onModeSwitcherButtonClick = {
                                navController.navigate(ModeSwitcher)
                            },
                            viewModel = viewModel,
                        )
                    }
                }
            //}

            LaunchedEffect(Unit) {
                delay(100)
                visibility = true
            }
        }
    }

    // Mode Switcher Screen
    composable<ModeSwitcher>(
        enterTransition = {
            fadeIn(animationSpec = tween(200))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(200))
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(200))
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(200))
        }
    ) {
        val viewModel: ModeSwitcherViewModel = koinViewModel()
        val screenState by viewModel.screenState.collectAsStateWithLifecycle()
        val context = LocalContext.current

        if (screenState != null) {
            ModeSwitcherScreen(
                currentLauncherProfile = screenState!!.activeProfile,
                profiles = screenState!!.profiles,
                onModeSettingsClick = {
                    viewModel.editActiveProfileSettings(it)
                    LauncherSettingsActivity.start(context)
                },
                onModeSelected = {
                    viewModel.updateActiveProfile(it)
                    navController.navigateUp()
                },
                onCancel = { navController.navigateUp() }
            )
        }
    }

    // Create new Mode
    createModeNavGraph(navController)

}