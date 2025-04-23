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

package com.fairphone.spring.launcher.data.serializer

import android.util.Log
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.fairphone.spring.launcher.data.model.Defaults.AIRPLANE_MODE_ENABLED
import com.fairphone.spring.launcher.data.model.Defaults.ALWAYS_ON_DISPLAY_ENABLED
import com.fairphone.spring.launcher.data.model.Defaults.BATTERY_SAVER_ENABLED
import com.fairphone.spring.launcher.data.model.Defaults.DEFAULT_ALLOWED_CONTACTS
import com.fairphone.spring.launcher.data.model.Defaults.DEFAULT_BG_COLOR1
import com.fairphone.spring.launcher.data.model.Defaults.DEFAULT_BG_COLOR2
import com.fairphone.spring.launcher.data.model.Defaults.DEFAULT_BLUE_LIGHT_FILTER_ENABLED
import com.fairphone.spring.launcher.data.model.Defaults.DEFAULT_DARK_MODE_SETTING
import com.fairphone.spring.launcher.data.model.Defaults.DEFAULT_ICON
import com.fairphone.spring.launcher.data.model.Defaults.DEFAULT_NAME
import com.fairphone.spring.launcher.data.model.Defaults.DEFAULT_REPEAT_CALL_ENABLED
import com.fairphone.spring.launcher.data.model.Defaults.DEFAULT_SOUND_SETTING
import com.fairphone.spring.launcher.data.model.Defaults.DEFAULT_VISIBLE_APPS
import com.fairphone.spring.launcher.data.model.Defaults.DEFAULT_WALLPAPER_ID
import com.fairphone.spring.launcher.data.model.Defaults.ECO_CHARGE_ENABLED
import com.fairphone.spring.launcher.data.model.Defaults.REDUCE_BRIGHTNESS_ENABLED
import com.fairphone.spring.launcher.data.model.Moment
import com.fairphone.spring.launcher.data.model.moment
import kotlinx.serialization.SerializationException
import java.io.InputStream
import java.io.OutputStream

object MomentSerializer : Serializer<Moment> {
    override val defaultValue: Moment
        get() = moment {
            name = DEFAULT_NAME
            icon = DEFAULT_ICON
            bgColor1 = DEFAULT_BG_COLOR1
            bgColor2 = DEFAULT_BG_COLOR2
            visibleApps.addAll(DEFAULT_VISIBLE_APPS)
            allowedContacts = DEFAULT_ALLOWED_CONTACTS
            repeatCallEnabled = DEFAULT_REPEAT_CALL_ENABLED
            wallpaperId = DEFAULT_WALLPAPER_ID
            darkModeSetting = DEFAULT_DARK_MODE_SETTING
            blueLightFilterEnabled = DEFAULT_BLUE_LIGHT_FILTER_ENABLED
            soundSetting = DEFAULT_SOUND_SETTING
            batterySaverEnabled = BATTERY_SAVER_ENABLED
            reduceBrightnessEnabled = REDUCE_BRIGHTNESS_ENABLED
            ecoChargeEnabled = ECO_CHARGE_ENABLED
            alwaysOnDisplayEnabled = ALWAYS_ON_DISPLAY_ENABLED
            airplaneModeEnabled = AIRPLANE_MODE_ENABLED
        }

    override suspend fun readFrom(input: InputStream): Moment {
        return try {
            Moment.parseFrom(input)
        } catch (e: SerializationException) {
            Log.e("MomentSerializer", "Error deserializing proto", e)
            throw CorruptionException("Cannot read proto.", e)
        }
    }

    override suspend fun writeTo(
        t: Moment,
        output: OutputStream
    ) {
        t.writeTo(output)
    }
}