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

import androidx.compose.ui.graphics.vector.ImageVector
import com.fairphone.spring.launcher.ui.ModeIcons
import com.fairphone.spring.launcher.ui.modeicons.Vector

fun Moment.getIconVector(): ImageVector {
    return when {
        icon == "Vector" -> ModeIcons.Vector
        else -> ModeIcons.Vector
    }
}

object Defaults {
    const val DEFAULT_NAME = "Essentials"
    const val DEFAULT_ICON = "Vector"
    const val DEFAULT_BG_COLOR1 = 0xB2C3D1D0
    const val DEFAULT_BG_COLOR2 = 0xB2FFBA63
    val DEFAULT_VISIBLE_APPS = listOf(
        "com.google.android.dialer",
        "com.google.android.apps.messaging",
        "com.fps.camera",
        "com.google.android.apps.maps",
    )
    val DEFAULT_ALLOWED_CONTACTS = AllowedContacts.NONE
    const val DEFAULT_REPEAT_CALL_ENABLED = true
    const val DEFAULT_WALLPAPER_ID = 0
    val DEFAULT_DARK_MODE_SETTING = DarkModeSetting.SYSTEM
    const val DEFAULT_BLUE_LIGHT_FILTER_ENABLED = false
    val DEFAULT_SOUND_SETTING = SoundSetting.FOLLOW_DEVICE_SETTINGS
    const val BATTERY_SAVER_ENABLED = false
    const val REDUCE_BRIGHTNESS_ENABLED = false
    const val ECO_CHARGE_ENABLED = false
    const val ALWAYS_ON_DISPLAY_ENABLED = false
    const val AIRPLANE_MODE_ENABLED = false
}
