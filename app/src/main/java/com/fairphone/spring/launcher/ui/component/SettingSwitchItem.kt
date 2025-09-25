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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
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
fun SettingSwitchItem(
    state: Boolean,
    title: String,
    subtitle: String,
    onClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(16.dp),
    ) {
        val onSurfaceDynamic =  MaterialTheme.colorScheme.onSurface
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
            Text(
                text = subtitle,
                style = FairphoneTypography.BodySmall,
                color = onSurfaceDynamic
            )
        }
        SwitchButton(
            state = state,
            onToggle = onClick
        )
    }
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
private fun SettingSwitchItem_Preview() {
    SpringLauncherTheme {
        Column {
            SettingSwitchItem(
                state = true,
                title = "Visible apps",
                subtitle = "Maps, Camera, Message, Chrome, Phone",
                onClick = {}
            )
            SettingSwitchItem(
                state = false,
                title = "Visible apps",
                subtitle = "Maps, Camera, Message, Chrome, Phone",
                onClick = {}
            )
        }

    }
}
