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
import com.fairphone.spring.launcher.data.model.protos.ContactType
import com.fairphone.spring.launcher.data.model.protos.LauncherProfile
import com.fairphone.spring.launcher.data.model.protos.SoundSetting
import com.fairphone.spring.launcher.data.model.protos.UiMode
import com.fairphone.spring.launcher.ui.icons.mode.fromString

fun LauncherProfile.getIconVector(): ImageVector =
    ImageVector.fromString(icon)


object Defaults {
    const val DEFAULT_ICON = "Spring"
    val DEFAULT_VISIBLE_APPS = listOf(
        AppPreset(Camera),
        AppPreset(DefaultBrowser),
        AppPreset(GoogleMaps),
        AppPreset(Messages),
        AppPreset(Phone),
    )
    const val Color_Transparent = 0x00000000
    val DEFAULT_ALLOWED_CONTACTS = ContactType.CONTACT_TYPE_STARRED
    const val DEFAULT_REPEAT_CALL_ENABLED = true
    const val DEFAULT_WALLPAPER_ID = 0
    val DEFAULT_DARK_MODE_SETTING = UiMode.UI_MODE_SYSTEM
    const val DEFAULT_BLUE_LIGHT_FILTER_ENABLED = false
    val DEFAULT_SOUND_SETTING = SoundSetting.SOUND_SETTING_FOLLOW_DEVICE_SETTINGS
    const val BATTERY_SAVER_ENABLED = false
    const val REDUCE_BRIGHTNESS_ENABLED = false
    val CONTACT_TYPE_LIST = listOf(
        ContactType.CONTACT_TYPE_EVERYONE,
        ContactType.CONTACT_TYPE_NONE,
        ContactType.CONTACT_TYPE_ALL_CONTACTS,
        ContactType.CONTACT_TYPE_STARRED,
        // TODO: Add custom type when contact list screen is implemented
        //ContactType.CONTACT_TYPE_CUSTOM,
    )
}
