/*
 * Copyright (C) 2025 FairPhone B.V.
 *
 * SPDX-FileCopyrightText: 2025. FairPhone B.V.
 *
 * SPDX-License-Identifier: EUPL-1.2
 */

package com.fairphone.spring.launcher.ui.screen.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.fairphone.spring.launcher.ui.component.SettingsTopBar
import com.fairphone.spring.launcher.ui.navigation.SettingsNavigation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LauncherSettingsScreen(
    onCloseSettings: () -> Unit
) {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            SettingsTopBar(
                navController = navController,
                onNavigateBack = {
                    if (!navController.navigateUp()) {
                        onCloseSettings()
                    }
                }
            )
        },
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                SettingsNavigation(
                    navController = navController,
                    onCloseSettings = onCloseSettings
                )
            }
        }
    )
}
