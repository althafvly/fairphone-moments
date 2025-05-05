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
import com.fairphone.spring.launcher.data.serializer.LauncherProfileSerializer
import com.fairphone.spring.launcher.ui.FP6Preview
import com.fairphone.spring.launcher.ui.component.LauncherProfileSettingsTopBar
import com.fairphone.spring.launcher.ui.component.ProfileNameEditorDialog
import com.fairphone.spring.launcher.ui.component.SettingListItem
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSettingsScreen(
    screenState: ProfileSettingsScreenState,
    onEditProfileName: (String) -> Unit,
    onNavigateToVisibleAppSettings: () -> Unit,
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
                currentLauncherProfile = screenState.profile,
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
                    title = stringResource(R.string.settings_title_visible_apps),
                    subtitle = screenState.visibleApps.joinToString(", ") { it.name },
                    onClick = onNavigateToVisibleAppSettings
                )
            }
        }

        ProfileNameEditorDialog(
            show = showProfileNameEditor,
            currentName = screenState.profile.name,
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
@FP6Preview()
fun ProfileSettings_Preview() {
    SpringLauncherTheme {
        ProfileSettingsScreen(
            screenState = ProfileSettingsScreenState(
                profile = LauncherProfileSerializer.defaultValue,
                visibleApps = emptyList()
            ),
            onEditProfileName = {},
            onNavigateToVisibleAppSettings = {})
    }
}
