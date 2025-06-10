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

package com.fairphone.spring.launcher.ui.icons.mode

import androidx.annotation.Keep
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.fairphone.spring.launcher.ui.FP6Preview
import com.fairphone.spring.launcher.ui.FP6PreviewDark
import com.fairphone.spring.launcher.ui.icons.ModeIcons
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme

@Keep
enum class ModeIcon(val imageVector: ImageVector) {
    Spring(ModeIcons.Spring),
    QualityTime(ModeIcons.QualityTime),
    DeepFocus(ModeIcons.DeepFocus),
    Recharge(ModeIcons.Recharge),
    Extra1(ModeIcons.Extra1),
    Extra2(ModeIcons.Extra2),
    Extra3(ModeIcons.Extra3),
    Extra4(ModeIcons.Extra4),
    Extra5(ModeIcons.Extra5),
    Extra6(ModeIcons.Extra6),
    Journey(ModeIcons.Journey),
    Vector(ModeIcons.Extra6);

    companion object {
        fun nextIcon(iconName: String): ModeIcon {
            val nbIcons = ModeIcon.entries.size
            val iconIndex = ModeIcon.entries.indexOf(ModeIcon.valueOf(iconName))
            return ModeIcon.entries[(iconIndex + 1) % nbIcons]
        }

        fun customIcons() = listOf(
            Extra1, Extra2, Extra3, Extra4, Extra5, Extra6
        )
    }
}

fun ImageVector.Companion.fromString(string: String): ImageVector =
    ModeIcon.entries.firstOrNull { it.name == string }?.imageVector ?: ModeIcon.Spring.imageVector

@Composable
fun ModeIcon_Preview() {
    SpringLauncherTheme {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
        ) {
            items(ModeIcon.entries) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Icon : ${it.name}", color = MaterialTheme.colorScheme.onBackground)
                    Image(
                        imageVector = it.imageVector,
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                    )
                }
            }
        }
    }
}

@Composable
@FP6Preview()
fun ModeIcon_LightPreview() {
    ModeIcon_Preview()
}

@Composable
@FP6PreviewDark()
fun ModeIcon_DarkPreview() {
    ModeIcon_Preview()
}


