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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fairphone.spring.launcher.ui.theme.FairphoneTypography
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme

@Composable
fun SettingListItem(
    title: String,
    subtitle: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: @Composable ((Boolean) -> Unit) = { enabled ->
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = if (enabled) {
                MaterialTheme.colorScheme.onSurface
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        )
    },
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable { onClick() }
            .background(color = if (enabled) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceDim)
            .padding(16.dp),
    ) {
        val onSurfaceDynamic =
            if (enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = FairphoneTypography.BodyMedium,
                color = onSurfaceDynamic
            )
            if (!subtitle.isNullOrEmpty()) {
                Text(
                    text = subtitle,
                    style = FairphoneTypography.BodySmall,
                    color = onSurfaceDynamic
                )
            }
        }

        icon(enabled)
    }
}

@Composable
fun SettingListItem_Preview() {
    SpringLauncherTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SettingListItem(
                title = "Visible apps",
                subtitle = "Maps, Camera, Message, Chrome, Phone",
                onClick = {}
            )
            SettingListItem(
                title = "Visible apps",
                subtitle = null,
                onClick = {}
            )
        }

    }
}

@Composable
@Preview(showBackground = true)
fun SettingListItem_Preview_Light() {
    SettingListItem_Preview()
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun SettingListItem_Preview_Dark() {
    SettingListItem_Preview()
}
