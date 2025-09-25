/*
 * Copyright (C) 2025 FairPhone B.V.
 *
 * SPDX-FileCopyrightText: 2025. FairPhone B.V.
 *
 * SPDX-License-Identifier: EUPL-1.2
 */

package com.fairphone.spring.launcher.ui.screen.mode.creator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.fairphone.spring.launcher.ui.theme.FairphoneTypography
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme

@Composable
fun NameYourMomentScreen(
    modeName: String,
    modeIcon: ModeIcon,
    onContinue: (String, ModeIcon) -> Unit
) {
    var isButtonContinueEnabled by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf(modeName) }
    var newIcon by remember { mutableStateOf(modeIcon) }
    var maxNameLength = 15
    var showError by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            ScreenHeader(
                title = stringResource(R.string.name_mode_screen_header)
            )

            ActionButton(
                icon = newIcon.imageVector,
                isSelected = false,
                type = ButtonType.RoundedCorner,
                size = ButtonSize.Big,
                onClick = {
                    newIcon = ModeIcon.nextIcon(newIcon.name)
                }
            )

            Column {
                DefaultTextField(
                    text = newName,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    onValueChange = {
                        showError = it.length >= maxNameLength
                        newName = it
                    },
                    showError = showError,
                    onDone = {
                        if (isButtonContinueEnabled) {
                            onContinue(newName, newIcon)
                        }
                    }
                )
                if (showError) {
                    Text(
                        text = stringResource(R.string.profile_name_max_length_error),
                        style = FairphoneTypography.BodySmall,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }
        }

        PrimaryButton(
            text = stringResource(R.string.bt_continue),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .height(80.dp)
                .padding(vertical = 16.dp),
            enabled = isButtonContinueEnabled,
        ) {
            onContinue(newName, newIcon)
        }
    }

    LaunchedEffect(newName, showError) {
        isButtonContinueEnabled = newName.isNotEmpty() && !showError
    }

    LaunchedEffect(Unit) {
        // Add the focus in th text field
        focusRequester.requestFocus()
    }
}

@Composable
private fun NameModeScreen_Preview() {
    SpringLauncherTheme {
        NameYourMomentScreen(
            "Essentials",
            ModeIcon.Extra1
        ) { name, newIcon -> }
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