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

import com.fairphone.spring.launcher.data.datasource.IMomentDataSource
import com.fairphone.spring.launcher.data.model.AllowedContacts
import com.fairphone.spring.launcher.data.model.DarkModeSetting
import com.fairphone.spring.launcher.data.model.Moment
import com.fairphone.spring.launcher.data.model.SoundSetting
import kotlinx.coroutines.flow.Flow

interface IMomentRepository {
    fun getActiveMoment(): Flow<Moment>
    suspend fun updateVisibleApps(visibleApps: List<String>)
    suspend fun updateName(name: String)
    suspend fun updateAllowedContacts(allowedContacts: AllowedContacts)
    suspend fun updateCustomContacts(customContacts: List<String>)
    suspend fun updateRepeatCallEnabled(enabled: Boolean)
    suspend fun updateAppNotifications(appNotifications: List<String>)
    suspend fun updateWallpaperId(wallpaperId: Int)
    suspend fun updateDarkModeSetting(darkModeSetting: DarkModeSetting)
    suspend fun updateBlueFilterEnabled(enabled: Boolean)
    suspend fun updateSoundSetting(soundSetting: SoundSetting)
    suspend fun updateBatterySaverEnabled(enabled: Boolean)
    suspend fun updateReduceBrightnessEnabled(enabled: Boolean)
    suspend fun updateEchoChargeEnabled(enabled: Boolean)
    suspend fun updateAlwaysOnDisplayEnabled(enabled: Boolean)
    suspend fun updateAirplaneModeEnabled(enabled: Boolean)
}

class MomentRepository(private val dataSource: IMomentDataSource) : IMomentRepository {
    override fun getActiveMoment(): Flow<Moment> = dataSource.getActiveMoment()

    override suspend fun updateVisibleApps(visibleApps: List<String>) =
        dataSource.updateVisibleApps(visibleApps)

    override suspend fun updateName(name: String) = dataSource.updateName(name)

    override suspend fun updateAllowedContacts(allowedContacts: AllowedContacts) =
        dataSource.updateAllowedContacts(allowedContacts)

    override suspend fun updateCustomContacts(customContacts: List<String>) =
        dataSource.updateCustomContacts(customContacts)

    override suspend fun updateRepeatCallEnabled(enabled: Boolean) =
        dataSource.updateRepeatCallEnabled(enabled)

    override suspend fun updateAppNotifications(appNotifications: List<String>) =
        dataSource.updateAppNotifications(appNotifications)

    override suspend fun updateWallpaperId(wallpaperId: Int) =
        dataSource.updateWallpaperId(wallpaperId)

    override suspend fun updateDarkModeSetting(darkModeSetting: DarkModeSetting) =
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
