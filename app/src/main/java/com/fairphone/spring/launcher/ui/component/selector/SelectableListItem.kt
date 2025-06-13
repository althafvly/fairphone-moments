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

package com.fairphone.spring.launcher.ui.component.selector

import android.content.res.Configuration
import android.graphics.drawable.Drawable
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.fairphone.spring.launcher.data.model.AppInfo
import com.fairphone.spring.launcher.ui.component.WorkAppBadge
import com.fairphone.spring.launcher.ui.theme.Color_FP_Brand_Lime
import com.fairphone.spring.launcher.ui.theme.FairphoneTypography
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme
import com.fairphone.spring.launcher.util.fakeApp

/**
 * Interface for an item that can be selected in a list.
 */
interface SelectableItem {
    /**
     * The unique identifier of the item.
     */
    val id: String

    /**
     * The name of the item.
     */
    val name: String

    /**
     * The icon of the item. Can be a [Drawable], [ImageVector] or any other type supported by [AsyncImage].
     */
    val icon: Any
}

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
        when (item) {
            is AppInfo -> {
                AppInfoIcon(appInfo = item, modifier = Modifier.size(40.dp))
            }

            else -> {
                AsyncImage(
                    model = item.icon,
                    contentDescription = item.name,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(32.dp),
                )
            }
        }

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
fun AppInfoIcon(appInfo: AppInfo, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = modifier

    ) {
        AsyncImage(
            model = appInfo.icon,
            contentDescription = appInfo.name,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .padding(top = 4.dp, start = 4.dp, end = 4.dp, bottom = 4.dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = CircleShape
                )
        )

        if (appInfo.isWorkApp) {
            WorkAppBadge(Modifier.size(16.dp))
        }
    }
}


@Composable
@Preview
fun AppInfoIcon_Preview() {
    SpringLauncherTheme {
        val context = LocalContext.current
        Row {
            AppInfoIcon(
                appInfo = context.fakeApp("App name"),
                modifier = Modifier.size(32.dp),
            )
            AppInfoIcon(
                appInfo = context.fakeApp("App name", isWorkApp = true),
                modifier = Modifier.size(32.dp),
            )
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
