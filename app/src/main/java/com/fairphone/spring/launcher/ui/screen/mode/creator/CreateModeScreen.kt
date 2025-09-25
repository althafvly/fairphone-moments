/*
 * Copyright (C) 2025 FairPhone B.V.
 *
 * SPDX-FileCopyrightText: 2025. FairPhone B.V.
 *
 * SPDX-License-Identifier: EUPL-1.2
 */

package com.fairphone.spring.launcher.ui.screen.mode.creator

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fairphone.spring.launcher.ui.navigation.CreateModeNavigation
import com.fairphone.spring.launcher.ui.navigation.SelectMode

@Composable
fun CreateModeScreen(
    onCloseModeCreator: () -> Unit,
    onModeCreated: () -> Unit,
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        topBar = {
            CreateMomentTopBar(
                hasBackButton = currentDestination?.hasRoute<SelectMode>() == false,
                onNavigateBack = { navController.navigateUp() },
                onNavigateClose = { onCloseModeCreator() }
            )
        },
        content = {
            Box(modifier = Modifier.padding(it)) {
                CreateModeNavigation(
                    navController = navController,
                    onModeCreated = onModeCreated,
                )
            }
        }
    )
}
