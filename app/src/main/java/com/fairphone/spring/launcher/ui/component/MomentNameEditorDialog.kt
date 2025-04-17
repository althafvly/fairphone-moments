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

package com.fairphone.spring.launcher.ui.component

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
fun MomentNameEditor(
    show: Boolean,
    currentName: String,
    onNameChange: (String) -> Unit,
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
                    text = stringResource(R.string.moment_name_editor_title),
                    style = FairphoneTypography.H5,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                Column { // Use Column in case you want to add more elements later
                    OutlinedTextField(
                        value = newName,
                        onValueChange = {
                            showError = it.length >= maxNameLength
                            newName = it
                        },
                        label = null,
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            unfocusedContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            cursorColor = MaterialTheme.colorScheme.onSurface,
                            focusedBorderColor = if (showError) {
                                MaterialTheme.colorScheme.error
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            },
                            unfocusedLabelColor = if (showError) {
                                MaterialTheme.colorScheme.error
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            },
                        )
                    )

                    if (showError) {
                        Text(
                            text = stringResource(R.string.moment_name_max_length_error),
                            style = FairphoneTypography.BodySmall,
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                }
            },
            confirmButton = {
                ConfirmButton(
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
fun MomentNameEditor_Preview() {
    SpringLauncherTheme {
        MomentNameEditor(
            show = true,
            currentName = "Test22342342233224",
            onNameChange = {},
            onConfirm = {},
            onDismiss = {}
        )
    }
}