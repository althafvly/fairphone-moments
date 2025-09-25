/*
 * Copyright (C) 2025 FairPhone B.V.
 *
 * SPDX-FileCopyrightText: 2025. FairPhone B.V.
 *
 * SPDX-License-Identifier: EUPL-1.2
 */

package com.fairphone.spring.launcher.ui.component

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fairphone.spring.launcher.R
import com.fairphone.spring.launcher.ui.theme.FairphoneTypography
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme

@Composable
fun ProfileNameEditorDialog(
    show: Boolean,
    currentName: String,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var newName by remember { mutableStateOf(currentName) }
    var maxNameLength = 15
    var showError by remember { mutableStateOf(false) }

    if (show) {
        AlertDialog(
            modifier = Modifier
                .shadow(
                    elevation = 40.dp,
                    spotColor = Color(0x1A000000),
                    ambientColor = Color(0x1A000000)
                )
                .background(color = MaterialTheme.colorScheme.surfaceContainer, shape = RoundedCornerShape(20.dp)),
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            textContentColor = MaterialTheme.colorScheme.onSurface,
            shape = RoundedCornerShape(20.dp),
            onDismissRequest = onDismiss,
            title = {
                Text(
                    text = stringResource(R.string.profile_name_editor_title),
                    style = FairphoneTypography.H5,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                Column {
                    DefaultTextField(
                        text = newName,
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
            },
            confirmButton = {
                PrimaryButton(
                    text = stringResource(R.string.bt_confirm),
                    onClick = {
                        if (newName.length < maxNameLength) {
                            onConfirm(newName)
                        }
                    },
                    modifier = Modifier.height(44.dp)
                )
            },
            dismissButton = {
                CancelButton(
                    onClick = onDismiss,
                    modifier = Modifier.height(44.dp)
                )
            }
        )
    }
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
fun ProfileNameEditorDialog_Preview() {
    SpringLauncherTheme {
        ProfileNameEditorDialog(
            show = true,
            currentName = "Test",
            onConfirm = {},
            onDismiss = {}
        )
    }
}