/*
 * Copyright (C) 2025 FairPhone B.V.
 *
 * SPDX-FileCopyrightText: 2025. FairPhone B.V.
 *
 * SPDX-License-Identifier: EUPL-1.2
 */

package com.fairphone.spring.launcher.ui.component.selector

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.fairphone.spring.launcher.data.model.ContactInfo
import com.fairphone.spring.launcher.data.model.SelectableItem
import com.fairphone.spring.launcher.ui.theme.Color_FP_Brand_Lime
import com.fairphone.spring.launcher.ui.theme.FairphoneTypography
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme
import com.fairphone.spring.launcher.util.fakeApp

@Composable
fun <T : SelectableItem> SelectableListItem(
    modifier: Modifier = Modifier,
    item: T,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .clickable { onClick() },
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

        Box(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(size = 33.dp)
                )
                .width(24.dp)
                .height(24.dp)
                .clip(CircleShape)
                .background(if (isSelected) Color_FP_Brand_Lime else Color.Transparent)

        ) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "selected",
                    tint = Color(0xFF1C1B1F),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .width(16.dp)
                        .height(16.dp)
                )
            }
        }

    }
}

@Composable
fun AppInfoListItem_Preview() {
    val context = LocalContext.current
    SpringLauncherTheme {
        Column {
            SelectableListItem(
                item = context.fakeApp("App name"),
                isSelected = true,
                onClick = {},
            )
            SelectableListItem(
                item = context.fakeApp("App name", isWorkApp = true),
                isSelected = true,
                onClick = {},
            )
            SelectableListItem(
                item = ContactInfo(
                    id = "",
                    name = "Contact Name",
                    icon = null,
                    contactUri = "".toUri(),
                ),
                isSelected = true,
                onClick = {},
            )
            SelectableListItem(
                item = ContactInfo(
                    id = "",
                    name = "Other Contact With Long Name",
                    icon = null,
                    contactUri = "".toUri(),
                ),
                isSelected = true,
                onClick = {},
            )
        }

    }
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
fun AppInfoListItem_PreviewLight() {
    AppInfoListItem_Preview()
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun AppInfoListItem_PreviewDark() {
    AppInfoListItem_Preview()
}
