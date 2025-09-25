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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.fairphone.spring.launcher.R
import com.fairphone.spring.launcher.data.model.AppInfo
import com.fairphone.spring.launcher.data.model.ContactInfo
import com.fairphone.spring.launcher.data.model.SelectableItem
import com.fairphone.spring.launcher.ui.theme.FairphoneTypography
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme

@Composable
fun <T : SelectableItem> SelectedListItem(
    item: T,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .width(65.dp)
    ) {
        Box(
            contentAlignment = Alignment.TopEnd,
            modifier = Modifier
                .padding(top = 4.dp, start = 4.dp, end = 4.dp)
        ) {
            SelectableItemIcon(
                item = item,
                modifier = Modifier.size(56.dp),
            )

            IconButton(
                onClick = { onDeleteClick() },
                modifier = Modifier
                    .border(
                        width = 0.5.dp,
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = CircleShape
                    )
                    .size(16.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = CircleShape
                    )
                    .clip(CircleShape)
                    .align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .size(8.dp)
                        .align(Alignment.Center)
                )
            }
        }

        Text(
            text = item.name,
            style = FairphoneTypography.LabelMedium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun SelectedListItem_Preview() {
    val context = LocalContext.current
    SpringLauncherTheme {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            SelectedListItem(
                item = ContactInfo(
                    id = "id",
                    name = "Contact name",
                    icon = ContextCompat.getDrawable(context, R.drawable.ic_launcher_foreground)!!,
                    contactUri = "".toUri(),
                ),
                onDeleteClick = {},
            )
            SelectedListItem<SelectableItem>(
                item = AppInfo(
                    name = "App name",
                    packageName = "package",
                    mainActivityClassName = "class",
                    icon = ContextCompat.getDrawable(context, R.drawable.ic_launcher_foreground)!!,
                    isWorkApp = true
                ),
                onDeleteClick = {}
            )
        }

    }
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
fun SelectedListItem_PreviewLight() {
    SelectedListItem_Preview()
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun SelectedListItem_PreviewDark() {
    SelectedListItem_Preview()
}