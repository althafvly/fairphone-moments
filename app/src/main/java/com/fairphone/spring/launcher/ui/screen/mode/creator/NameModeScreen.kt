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

package com.fairphone.spring.launcher.ui.screen.mode.add.name

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fairphone.spring.launcher.R
import com.fairphone.spring.launcher.ui.FP6Preview
import com.fairphone.spring.launcher.ui.FP6PreviewDark
import com.fairphone.spring.launcher.ui.component.ActionButton
import com.fairphone.spring.launcher.ui.component.ButtonSize
import com.fairphone.spring.launcher.ui.component.ButtonType
import com.fairphone.spring.launcher.ui.component.DefaultTextField
import com.fairphone.spring.launcher.ui.component.PrimaryButton
import com.fairphone.spring.launcher.ui.component.ScreenHeader
import com.fairphone.spring.launcher.ui.icons.mode.ModeIcon
import com.fairphone.spring.launcher.ui.screen.mode.ModeContainer
import com.fairphone.spring.launcher.ui.screen.mode.creator.CreateMomentTopBar
import com.fairphone.spring.launcher.ui.theme.FairphoneTypography
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme

@Composable
fun NameYourMomentScreen(
    modeName: String,
    modeIcon: ModeIcon,
    onNavigateBack: () -> Unit = {},
    onNavigateClose: () -> Unit = {},
    onContinue: (String) -> Unit = {}
) {
    var isButtonContinueEnabled by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf(modeName) }
    var maxNameLength = 15
    var showError by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    ModeContainer(onClick = onNavigateBack) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
                .padding(horizontal = 20.dp),
        ) {
            CreateMomentTopBar(
                hasBackButton = true,
                onNavigateBack = onNavigateBack,
                onNavigateClose = onNavigateClose
            )

            ScreenHeader(
                stringResource(R.string.name_mode_screen_header),
                modifier = Modifier.padding(horizontal = 36.dp),
                style = FairphoneTypography.H3
            )

            ActionButton(
                icon = modeIcon.imageVector,
                description = "Update moment",
                isSelected = false,
                type = ButtonType.RoundedCorner,
                size = ButtonSize.Big
            )

            Column {
                DefaultTextField(
                    text = newName,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp)
                        .focusRequester(focusRequester),
                    onValueChange = {
                        showError = it.length >= maxNameLength
                        newName = it
                    },
                    showError = showError,
                )
                if (showError) {
                    Text(
                        text = stringResource(R.string.profile_name_max_length_error),
                        style = FairphoneTypography.BodySmall,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1.0f))

            PrimaryButton(
                text = stringResource(R.string.bt_continue),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                enabled = isButtonContinueEnabled
            ) {
                onContinue(newName)
            }

            LaunchedEffect(newName, showError) {
                isButtonContinueEnabled = newName.isNotEmpty() && !showError
            }

            LaunchedEffect(Unit) {
                // Add the focus in th textfield
                focusRequester.requestFocus()
            }
        }
    }
}

@Composable
private fun NameModeScreen_Preview() {
    SpringLauncherTheme {
        NameYourMomentScreen(
            "Essentials",
            ModeIcon.Extra1
        )
    }
}

@Composable
@FP6Preview()
private fun NameModeScreen_LightPreview() {
    NameModeScreen_Preview()
}

@Composable
@FP6PreviewDark()
private fun ModeSwitcherScreen_DarkPreview() {
    NameModeScreen_Preview()
}