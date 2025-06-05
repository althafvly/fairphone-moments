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

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fairphone.spring.launcher.R
import com.fairphone.spring.launcher.ui.FP6Preview
import com.fairphone.spring.launcher.ui.FP6PreviewDark
import com.fairphone.spring.launcher.ui.component.SettingListItem
import com.fairphone.spring.launcher.ui.component.SettingSwitchItem
import com.fairphone.spring.launcher.ui.theme.FairphoneTypography
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme

@Composable
fun AllowedNotificationsSettingsScreen(
    screenState: AllowedNotificationsSettingsScreenState,
    onAppNotificationClick: () -> Unit,
    onAllowRepeatCallerClick: (Boolean) -> Unit,
    onReadNotificationPermissionClick: () -> Unit,
    onPostNotificationPermissionClick: () -> Unit,
) {
    when (screenState) {
        is AllowedNotificationsSettingsScreenState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize()
            )
        }

        is AllowedNotificationsSettingsScreenState.Success -> {
            AllowedNotificationsSettingsScreen(
                screenData = screenState.data,
                onAppNotificationClick = onAppNotificationClick,
                onAllowRepeatCallerClick = onAllowRepeatCallerClick,
                onReadNotificationPermissionClick = onReadNotificationPermissionClick,
                onPostNotificationPermissionClick = onPostNotificationPermissionClick
            )
        }
    }

}

@Composable
fun AllowedNotificationsSettingsScreen(
    screenData: AllowedNotificationSettingsData,
    onAppNotificationClick: () -> Unit,
    onAllowRepeatCallerClick: (Boolean) -> Unit,
    onReadNotificationPermissionClick: () -> Unit,
    onPostNotificationPermissionClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = stringResource(R.string.setting_subtitle_notification),
            style = FairphoneTypography.BodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 24.dp, start = 16.dp, end = 16.dp)
        )

        SettingListItem(
            title = stringResource(R.string.setting_notifications_app),
            subtitle = notificationSubtitle(screenData.allowedNotificationAppsCount),
            onClick = onAppNotificationClick,
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(size = 12.dp)
                )
                .clip(RoundedCornerShape(size = 12.dp))
        )

        SettingSwitchItem(
            state = screenData.repeatCallEnabled,
            title = stringResource(R.string.setting_notifications_allow_repeat_caller),
            subtitle = stringResource(R.string.setting_notifications_allow_repeat_caller_desc),
            onClick = onAllowRepeatCallerClick,
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(size = 12.dp)
                )
                .clip(RoundedCornerShape(size = 12.dp))
        )

        Text(
            text = stringResource(R.string.app_notifications_permission_header),
            style = FairphoneTypography.H4,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Text(
            text = stringResource(R.string.app_notifications_permission_text),
            style = FairphoneTypography.BodySmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        SettingListItem(
            title = stringResource(R.string.permission_toggle_title_notification_access),
            subtitle = stringResource(R.string.permission_toggle_subtitle_notification_access),
            onClick = { onReadNotificationPermissionClick() },
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            },
            modifier = Modifier.border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(size = 12.dp)
            )
                .clip(RoundedCornerShape(size = 12.dp)),
        )

        SettingListItem(
            title = stringResource(R.string.permission_toggle_title_post_notification),
            subtitle = stringResource(R.string.permission_toggle_subtitle_post_notification),
            onClick = { onPostNotificationPermissionClick() },
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            },
            modifier = Modifier.border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(size = 12.dp)
            )
                .clip(RoundedCornerShape(size = 12.dp)),
        )
    }
}

@Composable
fun notificationSubtitle(size: Int) =
    if (size == 0) {
        stringResource(R.string.setting_notifications_none)
    } else {
        pluralStringResource(R.plurals.setting_notifications_nb_allowed, size, size)
    }

@Composable
private fun AllowedNotificationsSettingsScreen_Preview() {
    val context = LocalContext.current
    SpringLauncherTheme {
        AllowedNotificationsSettingsScreen(
            screenState = AllowedNotificationsSettingsScreenState.Success(
                AllowedNotificationSettingsData(
                    allowedNotificationAppsCount = 3,
                    repeatCallEnabled = true,
                )
            ),
            onAppNotificationClick = {},
            onAllowRepeatCallerClick = {},
            onReadNotificationPermissionClick = {},
            onPostNotificationPermissionClick = {},

        )
    }
}

@Composable
@FP6Preview()
private fun AllowedNotificationsSettingsScreen_LightPreview() {
    AllowedNotificationsSettingsScreen_Preview()
}

@Composable
@FP6PreviewDark()
private fun AllowedNotificationsSettingsScreen_DarkPreview() {
    AllowedNotificationsSettingsScreen_Preview()
}