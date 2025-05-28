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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.fairphone.spring.launcher.R
import com.fairphone.spring.launcher.ui.theme.FairphoneTypography
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme

@Composable
fun ConfirmDialog(
    show: Boolean,
    title: String,
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    if (show) {
        AlertDialog(
            modifier = Modifier
                .shadow(
                    elevation = 40.dp,
                    spotColor = Color(0x1A000000),
                    ambientColor = Color(0x1A000000)
                )
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainer,
                    shape = RoundedCornerShape(20.dp)
                ),
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            textContentColor = MaterialTheme.colorScheme.onSurface,
            shape = RoundedCornerShape(20.dp),
            onDismissRequest = onDismiss,
            title = {
                Text(
                    text = title,
                    style = FairphoneTypography.H5,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                Text(
                    text = message,
                    style = FairphoneTypography.BodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            confirmButton = {
                PrimaryButton(
                    text = stringResource(R.string.bt_confirm),
                    onClick = onConfirm,
                    danger = true,
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
@Preview
private fun ConfirmDialog_DarkPreview() {
    SpringLauncherTheme {
        ConfirmDialog(
            show = true,
            title = stringResource(R.string.setting_button_delete_mode),
            message = stringResource(R.string.setting_delete_mode_confirm),
            onConfirm = {},
            onDismiss = {}
        )
    }
}

@Composable
@PreviewLightDark
private fun ConfirmDialog_LightPreview() {
    SpringLauncherTheme {
        ConfirmDialog(
            show = true,
            title = stringResource(R.string.setting_button_delete_mode),
            message = stringResource(R.string.setting_delete_mode_confirm),
            onConfirm = {},
            onDismiss = {}
        )
    }
}