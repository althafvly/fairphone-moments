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
import com.fairphone.spring.launcher.R
import com.fairphone.spring.launcher.data.model.protos.LauncherProfile
import com.fairphone.spring.launcher.data.model.protos.launcherProfile

const val CUSTOM_PROFILE_ID: String = "custom"

enum class Presets(val profile: LauncherProfile, val title: Int, val subtitle: Int) {
    Custom(
        profile = launcherProfile {
            id = CUSTOM_PROFILE_ID
            name = "Custom"
            icon = "Extra6"
            bgColor1 = 0xFFF27696
            bgColor2 = 0xFFF26E6E
        },
        title = R.string.mode_title_custom,
        subtitle = R.string.mode_subtitle_custom
    ),
    Essentials(
        profile = launcherProfile {
            id = "spring"
            name = "Spring"
            icon = "Spring"
            bgColor1 = 0xB2C3D1D0
            bgColor2 = 0xB2FFBA63
        },
        title = R.string.mode_title_essential,
        subtitle = R.string.mode_subtitle_essential
    ),
    DeepFocus(
        profile = launcherProfile {
            id = "deep_focus"
            name = "Deep focus"
            icon = "DeepFocus"
            bgColor1 = 0xFFF27696
            bgColor2 = 0xFFF26E6E
        },
        title = R.string.mode_title_deep_focus,
        subtitle = R.string.mode_subtitle_deep_focus
    ),
    Journey(
        profile = launcherProfile {
            id = "journey"
            name = "Journey"
            icon = "Journey"
            bgColor1 = 0xFFD8FF4F
            bgColor2 = 0xFF00433D
        },
        title = R.string.mode_title_journey,
        subtitle = R.string.mode_subtitle_journey
    ),
    Recharge(
        profile = launcherProfile {
            id = "recharge"
            name = "Recharge"
            icon = "Recharge"
            bgColor1 = 0xFF66A2DD
            bgColor2 = 0xFF2D9197
        }, title = R.string.mode_title_recharge,
        subtitle = R.string.mode_subtitle_recharge
    ),
    QualityTime(
        profile = launcherProfile {
            id = "qualitytime"
            name = "Quality Time"
            icon = "QualityTime"
            bgColor1 = 0xFF42CC60
            bgColor2 = 0xFFD8FF4F
        },
        title = R.string.mode_title_quality_time,
        subtitle = R.string.mode_subtitle_quality_time
    );

    companion object {
        val presetSelectionForNewMode = Presets.entries.minus(Essentials)
    }
}

fun LauncherProfile.colors(): Pair<Color, Color> {
    return Pair(Color(bgColor1), Color(bgColor2))
}