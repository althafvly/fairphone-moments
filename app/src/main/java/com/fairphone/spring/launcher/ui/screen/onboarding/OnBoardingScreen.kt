/*
 * Copyright (C) 2025 FairPhone B.V.
 *
 * SPDX-FileCopyrightText: 2025. FairPhone B.V.
 *
 * SPDX-License-Identifier: EUPL-1.2
 */

package com.fairphone.spring.launcher.ui.screen.onboarding

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fairphone.spring.launcher.ui.navigation.OnBoardingInit
import com.fairphone.spring.launcher.ui.navigation.OnBoardingNavigation
import com.fairphone.spring.launcher.ui.screen.mode.creator.CreateMomentTopBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun OnBoardingScreen(
    viewModel: OnBoardingStatusViewModel = koinViewModel(),
    onBoardingClose: () -> Unit
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        topBar = {
            CreateMomentTopBar(
                hasBackButton = currentDestination?.hasRoute<OnBoardingInit>() == false,
                onNavigateBack = { navController.navigateUp() },
                onNavigateClose = {
                    viewModel.cancelOnboarding()
                    onBoardingClose()
                }
            )
        },
        content = {
            Box(modifier = Modifier.padding(it)) {
                OnBoardingNavigation(
                    navController = navController,
                    onBoardingClose = {
                        viewModel.completeOnboarding()
                        onBoardingClose()
                    },
                )
            }
        }
    )
}