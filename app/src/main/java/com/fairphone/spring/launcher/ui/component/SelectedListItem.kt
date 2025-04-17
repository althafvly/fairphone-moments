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
import android.graphics.drawable.Drawable
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil3.compose.AsyncImage
import com.fairphone.spring.launcher.R
import com.fairphone.spring.launcher.ui.theme.FairphoneTypography
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme

@Composable
fun SelectedListItem(
    icon: Drawable,
    title: String,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.wrapContentHeight()
    ) {
        Box(
            contentAlignment = Alignment.TopEnd,
            modifier = Modifier
                .padding(top = 4.dp, start = 4.dp, end = 4.dp)
        ) {
            AsyncImage(
                model = icon,
                contentDescription = title,
                contentScale = ContentScale.Fit,
                modifier = Modifier.padding(top = 4.dp, start = 4.dp, end = 4.dp).size(40.dp),
            )

            IconButton(
                onClick = { onDeleteClick() },
                modifier = Modifier
                    .border(
                        width = 0.47619.dp,
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
            text = title,
            style = FairphoneTypography.LabelMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
fun SelectedAppInfoListItem_Preview() {
    SpringLauncherTheme {
        SelectedListItem(
            title = "App Name",
            icon = ContextCompat.getDrawable(
                LocalContext.current,
                R.drawable.ic_launcher_foreground
            )!!,
            onDeleteClick = {}
        )
    }
}