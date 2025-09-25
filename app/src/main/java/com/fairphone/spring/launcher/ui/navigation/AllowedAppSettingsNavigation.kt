/*
 * Copyright (C) 2025 FairPhone B.V.
 *
 * SPDX-FileCopyrightText: 2025. FairPhone B.V.
 *
 * SPDX-License-Identifier: EUPL-1.2
 */

package com.fairphone.spring.launcher.ui.navigation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.fairphone.spring.launcher.ui.screen.settings.notifications.AllowedAppsScreen
import com.fairphone.spring.launcher.ui.screen.settings.notifications.AllowedNotificationsAppsViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
object AllowedAppSettings

fun NavGraphBuilder.allowedAppSettingsNavGraph(navController: NavHostController) {
    // Allowed App Notification Settings Screen
    composable<AllowedAppSettings> {
        val viewModel: AllowedNotificationsAppsViewModel = koinViewModel()
        val screenState by viewModel.screenState.collectAsStateWithLifecycle()

        AllowedAppsScreen(
            screenState = screenState,
            onAllowAppSwitchClick = { app, value ->
                viewModel.updateAppNotificationRight(app, value)
            }
        )
    }
}