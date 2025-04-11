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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fairphone.spring.launcher.R
import com.fairphone.spring.launcher.data.model.Default
import com.fairphone.spring.launcher.ui.FP6Preview
import com.fairphone.spring.launcher.ui.component.SettingListItem
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun MomentSettings(
    onNavigateToVisibleAppSettings: () -> Unit,
    viewModel: MomentSettingsViewModel = koinViewModel()
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    screenState?.let {
        MomentSettings(
            screenState = it,
            onNavigateToVisibleAppSettings = onNavigateToVisibleAppSettings
        )
    }
}

@Composable
fun MomentSettings(
    screenState: MomentSettingsScreenState,
    onNavigateToVisibleAppSettings: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(size = 12.dp)
                )
                .clip(RoundedCornerShape(size = 12.dp))
                //.clickable { onNavigateToVisibleAppSettings() }
        ) {
            SettingListItem(
                title = stringResource(R.string.settings_title_visible_apps),
                subtitle = screenState.visibleApps.joinToString(", ") { it.name },
                onClick = onNavigateToVisibleAppSettings
            )
        }
    }
}

@Composable
@FP6Preview()
fun MomentSettings_Preview() {
    SpringLauncherTheme {
        MomentSettings(
            screenState = MomentSettingsScreenState(
                moment = Default.DefaultMoment,
                visibleApps = emptyList()
            ),
            onNavigateToVisibleAppSettings = {})
    }
}
