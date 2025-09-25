/*
 * Copyright (C) 2025 FairPhone B.V.
 *
 * SPDX-FileCopyrightText: 2025. FairPhone B.V.
 *
 * SPDX-License-Identifier: EUPL-1.2
 */

package com.fairphone.spring.launcher.ui.component.switcher

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fairphone.spring.launcher.data.model.SelectableItem
import com.fairphone.spring.launcher.ui.component.SwitchButton
import com.fairphone.spring.launcher.ui.component.selector.SelectableItemIcon
import com.fairphone.spring.launcher.ui.theme.FairphoneTypography
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme
import com.fairphone.spring.launcher.util.fakeApp

@Composable
fun <T : SelectableItem> SwitcherListItem(
    modifier: Modifier = Modifier,
    item: T,
    isChecked: Boolean,
    onClick: (Boolean) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
    ) {
        SelectableItemIcon(
            item = item,
            modifier = Modifier.size(48.dp),
        )

        Text(
            text = item.name,
            style = FairphoneTypography.BodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f),
        )

        SwitchButton(
            state = isChecked,
            onToggle = onClick
        )
    }
}

@Composable
fun SwitcherListItem_Preview() {
    val context = LocalContext.current
    SpringLauncherTheme {
        Column {
            SwitcherListItem(
                item = context.fakeApp("App name", isWorkApp = false),
                isChecked = true,
                onClick = {}
            )

            SwitcherListItem(
                item = context.fakeApp("Work app name", isWorkApp = true),
                isChecked = true,
                onClick = {}
            )
        }

    }
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun SwitcherListItem_PreviewDark() {
    SwitcherListItem_Preview()
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
fun SwitcherListItem_PreviewLight() {
    SwitcherListItem_Preview()
}