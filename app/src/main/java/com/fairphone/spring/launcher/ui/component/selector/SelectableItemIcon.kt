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

import androidx.annotation.ColorInt
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import androidx.core.net.toUri
import coil3.compose.AsyncImage
import com.fairphone.spring.launcher.data.model.AppInfo
import com.fairphone.spring.launcher.data.model.ContactInfo
import com.fairphone.spring.launcher.data.model.SelectableItem
import com.fairphone.spring.launcher.ui.component.WorkAppBadge
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme
import com.fairphone.spring.launcher.util.fakeApp
import kotlin.math.absoluteValue

@Composable
fun <T : SelectableItem> SelectableItemIcon(
    item: T,
    modifier: Modifier = Modifier,
) {
    when (item) {
        is AppInfo -> {
            AppInfoIcon(appInfo = item, modifier = modifier)
        }

        is ContactInfo -> {
            ContactInfoIcon(contactInfo = item, modifier = modifier)
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
            modifier = Modifier.iconBadge()
        )

        if (appInfo.isWorkApp) {
            WorkAppBadge(Modifier.size(16.dp))
        }
    }
}

@Composable
fun ContactInfoIcon(contactInfo: ContactInfo, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = modifier
    ) {
        if (contactInfo.icon != null) {
            AsyncImage(
                model = contactInfo.icon,
                contentDescription = contactInfo.name,
                contentScale = ContentScale.Fit,
                modifier = Modifier.iconBadge()
            )
        } else {
            InitialsAvatar(
                id = contactInfo.id,
                name = contactInfo.name,
                modifier = Modifier.iconBadge()
            )
        }

    }
}

@Composable
fun InitialsAvatar(
    id: String,
    name: String,
    modifier: Modifier = Modifier,
) {
    val color = remember(id, name) {
        Color("$id / $name".toHslColor())
    }

    val initials = remember(name) {
        name.split(" ").let {
            val firstNameInitial = it.first().first().toString()
            val lastNameInitial = it.getOrNull(1)?.first()?.toString() ?: ""

            firstNameInitial + lastNameInitial
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier,
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(SolidColor(color))
        }
        Text(
            text = initials.uppercase(),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
@Preview
fun SelectableItemIcon_Preview() {
    SpringLauncherTheme {
        val context = LocalContext.current
        Column {
            SelectableItemIcon(
                item = context.fakeApp("App name"),
                modifier = Modifier.size(48.dp),
            )
            SelectableItemIcon(
                item = context.fakeApp("App name", isWorkApp = true),
                modifier = Modifier.size(48.dp),
            )
            SelectableItemIcon(
                item = ContactInfo(
                    id = "",
                    name = "Contact name",
                    icon = null,
                    contactUri = "".toUri(),
                ),
                modifier = Modifier.size(48.dp),
            )
            SelectableItemIcon(
                item = ContactInfo(
                    id = "",
                    name = "Contact",
                    icon = null,
                    contactUri = "".toUri(),
                ),
                modifier = Modifier.size(48.dp),
            )
        }
    }
}

@Composable
fun Modifier.iconBadge(
    shape: Shape = CircleShape,
): Modifier = this
    .padding(top = 4.dp, start = 4.dp, end = 4.dp, bottom = 4.dp)
    .clip(shape)
    .border(
        width = 1.dp,
        color = MaterialTheme.colorScheme.outline,
        shape = shape
    )

@ColorInt
fun String.toHslColor(saturation: Float = 0.5f, lightness: Float = 0.4f): Int {
    val hue = fold(0) { acc, char -> char.code + acc * 37 } % 360
    return ColorUtils.HSLToColor(floatArrayOf(hue.absoluteValue.toFloat(), saturation, lightness))
}
