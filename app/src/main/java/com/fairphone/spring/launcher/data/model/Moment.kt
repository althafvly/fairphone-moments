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

package com.fairphone.spring.launcher.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.fairphone.spring.launcher.ui.ModeIcons
import com.fairphone.spring.launcher.ui.modeicons.Vector

data class Moment(
    val name: String,
    val icon: ImageVector,
    val bgColors: Pair<Color, Color>,
)

object Presets {
    val Essentials = Moment(
        name = "Fairphone moments",
        icon = ModeIcons.Vector,
        bgColors = Pair(
            Color(0xB2C3D1D0),
            Color(0xB2FFBA63),
        ),
    )
    val Journey = Moment(
        name = "Journey",
        icon = Icons.Filled.Settings,
        bgColors = Pair(
            Color(0xFFD8FF4F),
            Color(0xFF00433D),
        ),
    )
    val Recharge = Moment(
        name = "Recharge",
        icon = Icons.Filled.Settings,
        bgColors = Pair(
            Color(0xFF66A2DD),
            Color(0xFF2D9197),
        ),
    )
    val Balance = Moment(
        name = "Balance",
        icon = Icons.Filled.Settings,
        bgColors = Pair(
            Color(0xFF42CC60),
            Color(0xFFD8FF4F),
        ),
    )
    val DeepFocus = Moment(
        name = "Deep focus",
        icon = Icons.Filled.Settings,
        bgColors = Pair(
            Color(0xFFF27696),
            Color(0xFFF26E6E),
        ),
    )

    val All = listOf(
        Essentials,
        Journey,
        Recharge,
        Balance,
        DeepFocus,
    )
}
