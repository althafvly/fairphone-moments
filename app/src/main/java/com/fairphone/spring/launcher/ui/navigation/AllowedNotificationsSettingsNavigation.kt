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
