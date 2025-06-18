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

package com.fairphone.spring.launcher.ui.screen.settings.notifications

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.fairphone.spring.launcher.ui.FP6Preview
import com.fairphone.spring.launcher.ui.FP6PreviewDark
import com.fairphone.spring.launcher.ui.component.switcher.ItemSwitcherLayout
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme
import com.fairphone.spring.launcher.util.fakeApp

@Composable
fun AllowedAppsScreen(
    screenState: AllowedNotificationsAppsScreenState,
    onAllowAppSwitchClick: (String, Boolean) -> Unit
) {
    when (screenState) {
        is AllowedNotificationsAppsScreenState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize()
            )
        }

        is AllowedNotificationsAppsScreenState.Success -> {
            AllowedAppsScreen(
                screenData = screenState.data,
                onAllowAppSwitchClick = onAllowAppSwitchClick
            )
        }
    }
}

@Composable
fun AllowedAppsScreen(
    screenData: AllowedNotificationsAppsScreenData,
    onAllowAppSwitchClick: (String, Boolean) -> Unit
) {
        ItemSwitcherLayout(
            itemList = screenData.allNotificationApps,
            selectedItems = screenData.allowedNotificationApps,
            onItemClick = { app, value ->
                onAllowAppSwitchClick(app.id, value)
            }
        )
}


@Composable
private fun AllowedNotificationsAppsScreen_Preview() {
    val context = LocalContext.current
    SpringLauncherTheme {
        AllowedAppsScreen(
            screenState = AllowedNotificationsAppsScreenState.Success(
                AllowedNotificationsAppsScreenData(
                    profileId = "test",
                    allNotificationApps = listOf(
                        context.fakeApp("test"),
                        context.fakeApp("test1"),
                        context.fakeApp("test2")
                    ),
                    allowedNotificationApps = listOf(
                        context.fakeApp("test")
                    ),
                    repeatCallEnabled = true,
                )
            ),
            onAllowAppSwitchClick = {app, value -> },
        )
    }
}

@Composable
@FP6Preview()
private fun AllowedNotificationsAppsScreen_LightPreview() {
    AllowedNotificationsAppsScreen_Preview()
}

@Composable
@FP6PreviewDark()
private fun AllowedNotificationsAppsScreen_DarkPreview() {
    AllowedNotificationsAppsScreen_Preview()
}