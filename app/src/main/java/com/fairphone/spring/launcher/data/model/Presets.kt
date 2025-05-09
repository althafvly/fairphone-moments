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

import androidx.compose.ui.graphics.Color

object Presets {
    val Essentials = launcherProfile {
        name = "Spring"
        icon = "Vector"
        bgColor1 = 0xB2C3D1D0
        bgColor2 = 0xB2FFBA63
    }
    val Journey = launcherProfile {
        name = "Journey"
        icon = "Vector"
        bgColor1 = 0xFFD8FF4F
        bgColor2 = 0xFF00433D
    }
    val Recharge = launcherProfile {
        name = "Recharge"
        icon = "Vector"
        bgColor1 = 0xFF66A2DD
        bgColor2 = 0xFF2D9197
    }
    val Balance = launcherProfile {
        name = "Balance"
        icon = "Vector"
        bgColor1 = 0xFF42CC60
        bgColor2 = 0xFFD8FF4F
    }
    val DeepFocus = launcherProfile {
        name = "Deep focus"
        icon = "Vector"
        bgColor1 = 0xFFF27696
        bgColor2 = 0xFFF26E6E
    }
    val Custom = launcherProfile {
        name = "Custom"
        icon = "Vector"
        bgColor1 = 0xFFF27696
        bgColor2 = 0xFFF26E6E
    }

    val All = listOf(
        Essentials,
        Journey,
        Recharge,
        Balance,
        DeepFocus,
        Custom
    )
}

fun LauncherProfile.colors(): Pair<Color, Color> {
    return Pair(Color(bgColor1), Color(bgColor2))
}