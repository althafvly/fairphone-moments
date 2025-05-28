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

package com.fairphone.spring.launcher.ui.screen.mode.creator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.fairphone.spring.launcher.R
import com.fairphone.spring.launcher.data.model.AppInfo
import com.fairphone.spring.launcher.data.model.Presets
import com.fairphone.spring.launcher.ui.FP6Preview
import com.fairphone.spring.launcher.ui.FP6PreviewDark
import com.fairphone.spring.launcher.ui.component.ScreenHeader
import com.fairphone.spring.launcher.ui.screen.settings.apps.selector.ScreenData
import com.fairphone.spring.launcher.ui.screen.settings.apps.selector.VisibleAppSelectorScreen
import com.fairphone.spring.launcher.ui.screen.settings.apps.selector.VisibleAppSelectorScreenState
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme
import com.fairphone.spring.launcher.util.fakeApp

@Composable
fun ChooseAppsScreen(
    screenState: VisibleAppSelectorScreenState,
    filter: String,
    onAppFilterChanged: (String) -> Unit,
    onAppClick: (AppInfo) -> Unit,
    onAppDeselected: (AppInfo) -> Unit,
    onContinue: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ScreenHeader(
            title = stringResource(R.string.choose_app_screen_header),
            subtitle = stringResource(R.string.choose_app_screen_subtitle),
        )

        VisibleAppSelectorScreen(
            screenState = screenState,
            onAppClick = onAppClick,
            onAppDeselected = onAppDeselected,
            onConfirmAppSelection = {
                onContinue()
            },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun ChooseAppsScreen_Preview() {
    val context = LocalContext.current
    SpringLauncherTheme {
        ChooseAppsScreen(
            screenState = VisibleAppSelectorScreenState.Ready(
                data = ScreenData(
                    profileId = Presets.Essentials.profile.id,
                    appList = listOf(
                        context.fakeApp("test"),
                        context.fakeApp("test1"),
                        context.fakeApp("test2")
                    ),
                    visibleApps = emptyList(),
                    showConfirmButton = true,
                    showAppCounter = true,
                    showEmptyAppSelectedError = true,
                    showMaxAppSelectedError = true,
                    confirmButtonTextResource = R.string.bt_continue
                )
            ),
            filter = "",
            onAppClick = {},
            onAppDeselected = {},
            onAppFilterChanged = {},
            onContinue = {}
        )
    }
}

@Composable
@FP6Preview()
private fun ChooseAppsScreen_LightPreview() {
    ChooseAppsScreen_Preview()
}

@Composable
@FP6PreviewDark()
private fun ModeSwitcherScreen_DarkPreview() {
    ChooseAppsScreen_Preview()
}