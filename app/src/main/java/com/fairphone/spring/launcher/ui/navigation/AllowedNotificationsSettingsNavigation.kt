/*
 * Copyright (C) 2025 FairPhone B.V.
 *
 * SPDX-FileCopyrightText: 2025. FairPhone B.V.
 *
 * SPDX-License-Identifier: EUPL-1.2
 */

package com.fairphone.spring.launcher.ui.navigation

import android.Manifest
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.fairphone.spring.launcher.ui.screen.settings.notifications.NotificationsSettingsScreen
import com.fairphone.spring.launcher.ui.screen.settings.notifications.NotificationsSettingsViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
object NotificationSettings

fun NavGraphBuilder.notificationSettingsNavGraph(navController: NavHostController) {
    //Allowed Notification Settings Screen
    composable<NotificationSettings> {
        val viewModel: NotificationsSettingsViewModel = koinViewModel()
        val screenState by viewModel.screenState.collectAsStateWithLifecycle()
        val context = LocalContext.current
        val postNotificationLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = {
                Log.d("NotificationPermission", "Permission granted: $it")
            }
        )

        NotificationsSettingsScreen(
            screenState = screenState,
            onAllowRepeatCallsStateChanged = viewModel::onAllowRepeatCalls,
            onPermissionClick = {
               when(it.permissionName) {
                   Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE -> {
                       context.startActivity(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))
                   }
                   Manifest.permission.POST_NOTIFICATIONS -> {
                       postNotificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                   }
               }
            }
        )
    }
}
