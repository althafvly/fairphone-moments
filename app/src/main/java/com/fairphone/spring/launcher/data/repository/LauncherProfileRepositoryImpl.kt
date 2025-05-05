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

package com.fairphone.spring.launcher.data.repository

import android.content.Context
import com.fairphone.spring.launcher.data.datasource.ProfileDataSource
import com.fairphone.spring.launcher.data.model.ContactType
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
import com.fairphone.spring.launcher.data.model.LauncherProfile
import com.fairphone.spring.launcher.data.model.SoundSetting
import com.fairphone.spring.launcher.data.model.UiMode
import com.fairphone.spring.launcher.data.model.launcherProfile
import com.fairphone.spring.launcher.util.getDefaultBrowserPackageName
import kotlinx.coroutines.flow.Flow

interface LauncherProfileRepository {
    suspend fun initialize(context: Context)
    fun getActiveProfile(): Flow<LauncherProfile>
    suspend fun updateVisibleApps(visibleApps: List<String>)
    suspend fun updateName(name: String)
    suspend fun updateAllowedContacts(allowedContacts: ContactType)
    suspend fun updateCustomContacts(customContacts: List<String>)
    suspend fun updateRepeatCallEnabled(enabled: Boolean)
    suspend fun updateAppNotifications(appNotifications: List<String>)
    suspend fun updateWallpaperId(wallpaperId: Int)
    suspend fun updateDarkModeSetting(darkModeSetting: UiMode)
    suspend fun updateBlueFilterEnabled(enabled: Boolean)
    suspend fun updateSoundSetting(soundSetting: SoundSetting)
    suspend fun updateBatterySaverEnabled(enabled: Boolean)
    suspend fun updateReduceBrightnessEnabled(enabled: Boolean)
    suspend fun updateEchoChargeEnabled(enabled: Boolean)
    suspend fun updateAlwaysOnDisplayEnabled(enabled: Boolean)
    suspend fun updateAirplaneModeEnabled(enabled: Boolean)
}

class LauncherProfileRepositoryImpl(private val dataSource: ProfileDataSource) : LauncherProfileRepository {
    override suspend fun initialize(context: Context) {
        val defaultBrowser = getDefaultBrowserPackageName(context)
        val defaultVisibleApps = DEFAULT_VISIBLE_APPS + defaultBrowser
        val firstLauncherProfile = launcherProfile {
            name = DEFAULT_NAME
            icon = DEFAULT_ICON
            bgColor1 = DEFAULT_BG_COLOR1
            bgColor2 = DEFAULT_BG_COLOR2
            visibleApps.addAll(defaultVisibleApps)
            allowedContact = DEFAULT_ALLOWED_CONTACTS
            repeatCallEnabled = DEFAULT_REPEAT_CALL_ENABLED
            wallpaperId = DEFAULT_WALLPAPER_ID
            uiMode = DEFAULT_DARK_MODE_SETTING
            blueLightFilterEnabled = DEFAULT_BLUE_LIGHT_FILTER_ENABLED
            soundSetting = DEFAULT_SOUND_SETTING
            batterySaverEnabled = BATTERY_SAVER_ENABLED
            reduceBrightnessEnabled = REDUCE_BRIGHTNESS_ENABLED
            ecoChargeEnabled = ECO_CHARGE_ENABLED
            alwaysOnDisplayEnabled = ALWAYS_ON_DISPLAY_ENABLED
            airplaneModeEnabled = AIRPLANE_MODE_ENABLED
        }
        dataSource.updateLauncherProfile(firstLauncherProfile)
    }

    override fun getActiveProfile(): Flow<LauncherProfile> = dataSource.getActiveProfile()

    override suspend fun updateVisibleApps(visibleApps: List<String>) =
        dataSource.updateVisibleApps(visibleApps)

    override suspend fun updateName(name: String) {
    dataSource.updateName(name)
    }

    override suspend fun updateAllowedContacts(allowedContacts: ContactType) =
        dataSource.updateAllowedContacts(allowedContacts)

    override suspend fun updateCustomContacts(customContacts: List<String>) =
        dataSource.updateCustomContacts(customContacts)

    override suspend fun updateRepeatCallEnabled(enabled: Boolean) =
        dataSource.updateRepeatCallEnabled(enabled)

    override suspend fun updateAppNotifications(appNotifications: List<String>) =
        dataSource.updateAppNotifications(appNotifications)

    override suspend fun updateWallpaperId(wallpaperId: Int) =
        dataSource.updateWallpaperId(wallpaperId)

    override suspend fun updateDarkModeSetting(darkModeSetting: UiMode) =
        dataSource.updateDarkModeSetting(darkModeSetting)

    override suspend fun updateBlueFilterEnabled(enabled: Boolean) =
        dataSource.updateBlueFilterEnabled(enabled)

    override suspend fun updateSoundSetting(soundSetting: SoundSetting) =
        dataSource.updateSoundSetting(soundSetting)

    override suspend fun updateBatterySaverEnabled(enabled: Boolean) =
        dataSource.updateBatterySaverEnabled(enabled)

    override suspend fun updateReduceBrightnessEnabled(enabled: Boolean) =
        dataSource.updateReduceBrightnessEnabled(enabled)

    override suspend fun updateEchoChargeEnabled(enabled: Boolean) =
        dataSource.updateEchoChargeEnabled(enabled)

    override suspend fun updateAlwaysOnDisplayEnabled(enabled: Boolean) =
        dataSource.updateAlwaysOnDisplayEnabled(enabled)

    override suspend fun updateAirplaneModeEnabled(enabled: Boolean) =
        dataSource.updateAirplaneModeEnabled(enabled)
}
