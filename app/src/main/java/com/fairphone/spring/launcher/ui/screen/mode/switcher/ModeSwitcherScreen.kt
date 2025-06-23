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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fairphone.spring.launcher.R
import com.fairphone.spring.launcher.data.model.Mock_Profile
import com.fairphone.spring.launcher.data.model.protos.LauncherProfile
import com.fairphone.spring.launcher.ui.FP6Preview
import com.fairphone.spring.launcher.ui.FP6PreviewDark
import com.fairphone.spring.launcher.ui.component.ActionButton
import com.fairphone.spring.launcher.ui.component.ScreenHeader
import com.fairphone.spring.launcher.ui.screen.mode.switcher.component.ModeSwitcherButton
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme
import com.fairphone.spring.launcher.util.Constants

@Composable
fun ModeSwitcherScreen(
    currentLauncherProfile: LauncherProfile,
    profiles: List<LauncherProfile>,
    isMaxProfileCountReached: Boolean,
    onModeSelected: (LauncherProfile) -> Unit = {},
    onModeSettingsClick: (LauncherProfile) -> Unit = {},
    onCreateMomentClick: () -> Unit = {},
    onCancel: () -> Unit = {}
) {
//    ModeContainer(endColor = Color(currentLauncherProfile.bgColor2), onClick = onCancel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = onCancel)
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

        if (!isMaxProfileCountReached) {
            ActionButton(
                icon = Icons.Filled.Add,
                labelText = stringResource(R.string.bt_create_moment),
                modifier = Modifier.padding(bottom = 56.dp),
                onClick = onCreateMomentClick
            )
        } else {
            Text(
                text = stringResource(R.string.maximum_moments_reached_label),
                modifier = Modifier
                    .padding(bottom = 56.dp)
                    .width(160.dp),
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 10.sp,
                    color = Color.White,
                    fontStyle = FontStyle.Italic
                )
            )
        }
    }
}

@Composable
private fun ModeSwitcherScreen_Preview(profileCount: Int) {
    val profiles = List(profileCount) { Mock_Profile }
    SpringLauncherTheme {
        ModeSwitcherScreen(
            currentLauncherProfile = Mock_Profile,
            profiles = profiles,
            isMaxProfileCountReached = profiles.size >= Constants.MAX_PROFILE_COUNT,
        )
    }
}

@Composable
@FP6Preview()
@FP6PreviewDark()
private fun ModeSwitcherScreen_Preview_One_Profile() {
    ModeSwitcherScreen_Preview(profileCount = 1)
}

@Composable
@FP6Preview()
@FP6PreviewDark()
private fun ModeSwitcherScreen_Preview_Multiple_Profiles() {
    ModeSwitcherScreen_Preview(profileCount = 3)
}

@Composable
@FP6Preview()
@FP6PreviewDark()
private fun ModeSwitcherScreen_Preview_WithMaxProfileCount() {
    ModeSwitcherScreen_Preview(profileCount = 6)
}
