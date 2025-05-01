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

package com.fairphone.spring.launcher.ui.screen.settings.main

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fairphone.spring.launcher.R
import com.fairphone.spring.launcher.data.model.AppInfo
import com.fairphone.spring.launcher.data.model.LauncherProfile
import com.fairphone.spring.launcher.data.serializer.LauncherProfileSerializer
import com.fairphone.spring.launcher.ui.FP6PreviewDark
import com.fairphone.spring.launcher.ui.component.LauncherProfileSettingsTopBar
import com.fairphone.spring.launcher.ui.component.ProfileNameEditorDialog
import com.fairphone.spring.launcher.ui.component.SettingListItem
import com.fairphone.spring.launcher.ui.screen.settings.contacts.getNameResId
import com.fairphone.spring.launcher.ui.theme.FairphoneTypography
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme

@Composable
fun ProfileSettingsScreen(
    screenState: ProfileSettingsScreenState,
    onEditProfileName: (String) -> Unit,
    onNavigateToVisibleAppSettings: () -> Unit,
    onNavigateToAllowedContactSettings: () -> Unit,
) {
    when (screenState) {
        is ProfileSettingsScreenState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize()
            )
        }

        is ProfileSettingsScreenState.Success -> {
            ProfileSettingsScreen(
                profile = screenState.profile,
                visibleApps = screenState.visibleApps,
                onEditProfileName = onEditProfileName,
                onNavigateToVisibleAppSettings = onNavigateToVisibleAppSettings,
                onNavigateToAllowedContactSettings = onNavigateToAllowedContactSettings
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSettingsScreen(
    profile: LauncherProfile,
    visibleApps: List<AppInfo>,
    onEditProfileName: (String) -> Unit,
    onNavigateToVisibleAppSettings: () -> Unit,
    onNavigateToAllowedContactSettings: () -> Unit,
) {
    var showProfileNameEditor by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            LauncherProfileSettingsTopBar(
                currentLauncherProfile = profile,
                onEditLauncherProfileName = {
                    showProfileNameEditor = true
                },
                //modifier = Modifier.windowInsetsPadding(TopAppBarDefaults.windowInsets)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(size = 12.dp)
                    )
                    .clip(RoundedCornerShape(size = 12.dp))
            ) {
                SettingListItem(
                    title = stringResource(R.string.setting_title_visible_apps),
                    subtitle = visibleApps.joinToString(", ") { it.name },
                    onClick = onNavigateToVisibleAppSettings
                )
            }

            Text(
                text = stringResource(R.string.setting_title_customization_settings),
                style = FairphoneTypography.BodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 24.dp, bottom = 16.dp)
            )

            // Other settings
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(size = 12.dp)
                    )
                    .clip(RoundedCornerShape(size = 12.dp))
            ) {
                val contactType = stringResource(profile.allowedContacts.getNameResId())
                SettingListItem(
                    title = stringResource(R.string.setting_title_allowed_contacts),
                    subtitle = stringResource(
                        R.string.setting_subtitle_allowed_contacts,
                        contactType
                    ),
                    onClick = onNavigateToAllowedContactSettings
                )
            }
        }

        ProfileNameEditorDialog(
            show = showProfileNameEditor,
            currentName = profile.name,
            onConfirm = {
                onEditProfileName(it)
                showProfileNameEditor = false
            },
            onDismiss = {
                showProfileNameEditor = false
            }
        )
    }
}


@Composable
@FP6PreviewDark()
fun ProfileSettings_Preview() {
    SpringLauncherTheme {
        ProfileSettingsScreen(
            profile = LauncherProfileSerializer.defaultValue,
            visibleApps = emptyList(),
            onEditProfileName = {},
            onNavigateToVisibleAppSettings = {},
            onNavigateToAllowedContactSettings = {},
        )
    }
}
