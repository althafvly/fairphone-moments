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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fairphone.spring.launcher.R
import com.fairphone.spring.launcher.data.model.LauncherProfile
import com.fairphone.spring.launcher.data.model.Presets
import com.fairphone.spring.launcher.ui.FP6Preview
import com.fairphone.spring.launcher.ui.FP6PreviewDark
import com.fairphone.spring.launcher.ui.component.ScreenHeader
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme

@Composable
fun SelectModeScreen(
    profiles: List<Presets>,
    onModeSettingsClick: (LauncherProfile) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(40.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ScreenHeader(
            title = stringResource(R.string.add_mode_screen_header)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .padding(horizontal = 20.dp)
        ) {
            profiles.forEach { mode ->
                SelectModeButton(
                    presetProfile = mode,
                    onModeSettingsClick = onModeSettingsClick,
                )
            }
        }
    }
}

@Composable
private fun ModeSwitcherScreen_Preview() {
    SpringLauncherTheme {
        SelectModeScreen(Presets.entries, {})
    }
}

@Composable
@FP6Preview()
private fun ModeSwitcherScreen_LightPreview() {
    ModeSwitcherScreen_Preview()
}

@Composable
@FP6PreviewDark()
private fun ModeSwitcherScreen_DarkPreview() {
    ModeSwitcherScreen_Preview()
}