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

package com.fairphone.spring.launcher.ui.screen.settings.apps

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fairphone.spring.launcher.R
import com.fairphone.spring.launcher.data.model.AppInfo
import com.fairphone.spring.launcher.ui.component.AppInfoListItem
import com.fairphone.spring.launcher.ui.theme.FairphoneTypography
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme
import com.fairphone.spring.launcher.util.fakeApp

@Composable
fun VisibleAppSettingsScreen(
    screenState: VisibleAppSettingsScreenState,
    onChangeAppsClick: () -> Unit,
) {
    when (screenState) {
        is VisibleAppSettingsScreenState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize()
            )
        }

        is VisibleAppSettingsScreenState.Ready -> {
            VisibleAppSettingsScreen(
                visibleApps = screenState.visibleApps,
                onChangeAppsClick = onChangeAppsClick
            )
        }
    }
}

@Composable
fun VisibleAppSettingsScreen(
    visibleApps: List<AppInfo>,
    onChangeAppsClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text(
            text = stringResource(R.string.setting_header_visible_apps),
            style = FairphoneTypography.BodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(start = 36.dp, end = 36.dp, top = 24.dp)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            visibleApps.forEach { appInfo ->
                AppInfoListItem(
                    icon = appInfo.icon,
                    name = appInfo.name,
                    isWorkApp = appInfo.isWorkApp
                )
            }
        }

        Box(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(size = 100.dp)
                )
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(size = 100.dp)
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .clickable {
                    onChangeAppsClick()
                }
        ) {
            Text(
                text = stringResource(R.string.bt_change_apps),
                style = FairphoneTypography.LabelMedium,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
@Preview
fun VisibleAppsSettingsScreen_Preview() {
    val context = LocalContext.current
    SpringLauncherTheme {
        VisibleAppSettingsScreen(
            visibleApps = listOf(
                context.fakeApp("app 1"),
                context.fakeApp("app 2"),
                context.fakeApp("app 3"),
                context.fakeApp("app 4")
            ),
            onChangeAppsClick = {}
        )
    }
}