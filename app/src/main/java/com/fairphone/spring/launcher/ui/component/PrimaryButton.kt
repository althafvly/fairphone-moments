/*
 * Copyright (C) 2025 FairPhone B.V.
 *
 * SPDX-FileCopyrightText: 2025. FairPhone B.V.
 *
 * SPDX-License-Identifier: EUPL-1.2
 */

package com.fairphone.spring.launcher.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fairphone.spring.launcher.R
import com.fairphone.spring.launcher.ui.FP6Preview
import com.fairphone.spring.launcher.ui.FP6PreviewDark
import com.fairphone.spring.launcher.ui.theme.FairphoneTypography
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme
import com.fairphone.spring.launcher.ui.theme.errorColor
import com.fairphone.spring.launcher.ui.theme.onErrorColor

@Composable
fun PrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    danger: Boolean = false,
    visible: Boolean = true,
    onClick: () -> Unit,
) {
    if (visible) {
        Button(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (danger) errorColor else MaterialTheme.colorScheme.primaryContainer,
                contentColor = if (danger) onErrorColor else MaterialTheme.colorScheme.onPrimaryContainer
            )
        ) {
            Text(
                text = text,
                style = FairphoneTypography.ButtonDefault,
                color = if (danger) onErrorColor else MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
fun PrimaryButton_Preview() {
    SpringLauncherTheme {
        Column {
            PrimaryButton(
                text = stringResource(R.string.bt_confirm),
                modifier = Modifier.fillMaxWidth()
            ) {}

            PrimaryButton(
                text = stringResource(R.string.bt_confirm),
                danger = true,
                modifier = Modifier.fillMaxWidth()
            ) {}
        }
    }
}

@Composable
@FP6Preview()
fun PrimaryButton_LightPreview() {
    PrimaryButton_Preview()
}

@Composable
@FP6PreviewDark()
fun PrimaryButton_DarkPreview() {
    PrimaryButton_Preview()
}