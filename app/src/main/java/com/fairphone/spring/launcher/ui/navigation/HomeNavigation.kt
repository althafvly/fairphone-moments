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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import com.fairphone.spring.launcher.data.prefs.UsageMode
import com.fairphone.spring.launcher.ui.component.AnimatedBackground
import com.fairphone.spring.launcher.ui.screen.home.HomeScreen
import com.fairphone.spring.launcher.ui.screen.home.HomeScreenViewModel
import com.fairphone.spring.launcher.ui.screen.mode.creator.CreateModeScreen
import com.fairphone.spring.launcher.ui.screen.mode.switcher.ModeSwitcherScreen
import com.fairphone.spring.launcher.ui.screen.mode.switcher.ModeSwitcherViewModel
import com.fairphone.spring.launcher.ui.screen.onboarding.OnBoardingScreen
import com.fairphone.spring.launcher.util.FairphoneWebViewScreen
import com.fairphone.spring.launcher.util.MOMENTS_DEMO_URL
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
object Home

@Serializable
object ModeSwitcher

@Serializable
object OnBoarding

@Serializable
object ModeCreator

@Serializable
object FairphoneDemoWebView

@Composable
fun HomeNavigation(
    navController: NavHostController = rememberNavController(),
    isFirstLaunch: Boolean
) = NavHost(
    navController = navController,
    startDestination = Home
) {
    composable<Home> (
        exitTransition = {
            fadeOut(animationSpec = tween(200))
        }
    ){
        val viewModel: HomeScreenViewModel = koinViewModel()
        val screenState by viewModel.screenState.collectAsStateWithLifecycle()

        var visibility by remember { mutableStateOf(false) }

        if (screenState != null) {
            LaunchedEffect(Unit) {
                delay(200)
                visibility = true
            }
            AnimatedVisibility(
                visible = visibility,
                enter = if(isFirstLaunch) expandVertically(
                    expandFrom = Alignment.Top,
                    animationSpec = tween(
                        durationMillis = 300
                    )
                ) else fadeIn(animationSpec = tween(200)) //exit isn't shown, we need to understand why
            ) {
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
                                if (screenState?.appUsageMode == UsageMode.ON_BOARDING) {
                                    navController.navigate(OnBoarding)
                                } else {
                                    navController.navigate(ModeSwitcher)
                                }
                            },
                            onDemoCardClick = {
                                navController.navigate(FairphoneDemoWebView)
                            },
                            viewModel = viewModel,
                        )
                    }
                }
            }
        }
    }


    composable<FairphoneDemoWebView> {
        FairphoneWebViewScreen(
            url = MOMENTS_DEMO_URL,
            showCloseButton = true,
            hideHeaderAndFooter = true,
            disableUrlLoading = true,
            onBackPressed = { navController.navigateUp() }
        )
    }

    // Create new Mode
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
            AnimatedBackground(
                colors = screenState!!.activeProfile.colors(),
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
            ) {
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
                    onCancel = {
                        navController.navigateUp()
                    },
                    onCreateMomentClick = {
                        navController.navigate(ModeCreator)
                    }
                )
            }

        }
    }

        composable<ModeCreator>(
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
            val context = LocalContext.current
            CreateModeScreen(
                onCloseModeCreator = {
                    navController.popBackStack()
                },
                onModeCreated = {
                    navController.popBackStack()
                    LauncherSettingsActivity.start(context)
                }
            )
        }

    // Create new Mode
    composable<OnBoarding>(
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
        OnBoardingScreen(
            onBoardingClose = {
                navController.navigateUp()
            }
        )
    }
}
