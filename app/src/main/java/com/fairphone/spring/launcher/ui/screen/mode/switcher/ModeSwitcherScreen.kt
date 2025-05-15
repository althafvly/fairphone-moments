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

package com.fairphone.spring.launcher.ui.screen.mode.switcher

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fairphone.spring.launcher.R
import com.fairphone.spring.launcher.data.model.LauncherProfile
import com.fairphone.spring.launcher.data.model.Presets
import com.fairphone.spring.launcher.ui.FP6Preview
import com.fairphone.spring.launcher.ui.FP6PreviewDark
import com.fairphone.spring.launcher.ui.component.ScreenHeader
import com.fairphone.spring.launcher.ui.screen.mode.ModeContainer
import com.fairphone.spring.launcher.ui.screen.mode.switcher.component.ModeSwitcherButton
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme

@Composable
fun ModeSwitcherScreen(
    currentLauncherProfile: LauncherProfile,
    profiles: List<LauncherProfile>,
    onModeSelected: (LauncherProfile) -> Unit = {},
    onModeSettingsClick: (LauncherProfile) -> Unit = {},
    onCreateMomentClick: () -> Unit = {},
    onCancel: () -> Unit = {}
) {
    ModeContainer(endColor = Color(currentLauncherProfile.bgColor2), onClick = onCancel) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 80.dp, start = 20.dp, end = 20.dp),
        ) {
            ScreenHeader(stringResource(R.string.mode_switcher_screen_header))

            Spacer(modifier = Modifier.weight(1.0f))

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                profiles.forEach { mode ->
                    ModeSwitcherButton(
                        profile = mode,
                        isSelected = mode == currentLauncherProfile,
                        modifier = Modifier.padding(vertical = 4.dp),
                        onModeSettingsClick = onModeSettingsClick
                    ) {
                        onModeSelected(mode)
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1.0f))

            if (profiles.size < 6) {
                ActionButton(
                    icon = Icons.Filled.Add,
                    description = "Add Moment",
                    displayLabel = true,
                    modifier = Modifier.padding(bottom = 56.dp),
                    onClick = onCreateMomentClick
                )
            }

        }
    }
}

@Composable
private fun ModeSwitcherScreen_Preview(nbMoment: Int = 2) {
    SpringLauncherTheme {
        ModeSwitcherScreen(
            currentLauncherProfile = Presets.Essentials.profile,
            profiles = Presets.entries.map { it.profile }.subList(0, nbMoment),
        )
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
    ModeSwitcherScreen_Preview(nbMoment = 3)
}

@Composable
@FP6Preview()
private fun ModeSwitcherFullScreen_LightPreview() {
    ModeSwitcherScreen_Preview(nbMoment = 6)
}

@Composable
@FP6PreviewDark()
private fun ModeSwitcherFullScreen_DarkPreview() {
    ModeSwitcherScreen_Preview(nbMoment = 5)
}