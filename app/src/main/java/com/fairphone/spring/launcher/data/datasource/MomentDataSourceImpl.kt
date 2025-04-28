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

package com.fairphone.spring.launcher.data.datasource

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import com.fairphone.spring.launcher.data.model.AllowedContacts
import com.fairphone.spring.launcher.data.model.DarkModeSetting
import com.fairphone.spring.launcher.data.model.Moment
import com.fairphone.spring.launcher.data.model.SoundSetting
import com.fairphone.spring.launcher.di.momentDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.io.IOException

interface MomentDataSource {
    fun getActiveMoment(): Flow<Moment>
    suspend fun updateMoment(moment: Moment)
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

class MomentDataSourceImpl(context: Context) : MomentDataSource {
    private val dataStore: DataStore<Moment> = context.momentDataStore

    override fun getActiveMoment(): Flow<Moment> {
        return dataStore.data
            .catch { exception ->
                // dataStore.data throws an IOException when an error is encountered when reading data
                if (exception is IOException) {
                    Log.e("MomentRepository", "Error reading sort order preferences.", exception)
                    emit(Moment.getDefaultInstance())
                } else {
                    throw exception
                }
            }
    }

    override suspend fun updateMoment(moment: Moment) {
        dataStore.updateData { moment }
    }

    override suspend fun updateVisibleApps(visibleApps: List<String>) {
        dataStore.updateData { moment ->
            moment.toBuilder()
                .clearVisibleApps()
                .addAllVisibleApps(visibleApps)
                .build()
        }
    }

    override suspend fun updateName(name: String) {
        dataStore.updateData { moment ->
            moment.toBuilder()
                .setName(name)
                .build()
        }
    }

    override suspend fun updateAllowedContacts(allowedContacts: AllowedContacts) {
        dataStore.updateData { moment ->
            moment.toBuilder()
                .setAllowedContacts(allowedContacts)
                .build()
        }
    }

    override suspend fun updateCustomContacts(customContacts: List<String>) {
        dataStore.updateData { moment ->
            moment.toBuilder()
                .clearCustomContacts()
                .addAllCustomContacts(customContacts)
                .build()
        }
    }

    override suspend fun updateRepeatCallEnabled(enabled: Boolean) {
        dataStore.updateData { moment ->
            moment.toBuilder()
                .setRepeatCallEnabled(enabled)
                .build()
        }

    }

    override suspend fun updateAppNotifications(appNotifications: List<String>) {
        dataStore.updateData { moment ->
            moment.toBuilder()
                .clearAppNotifications()
                .addAllAppNotifications(appNotifications)
                .build()
        }
    }

    override suspend fun updateWallpaperId(wallpaperId: Int) {
        dataStore.updateData { moment ->
            moment.toBuilder()
                .setWallpaperId(wallpaperId)
                .build()
        }
    }

    override suspend fun updateDarkModeSetting(darkModeSetting: DarkModeSetting) {
        dataStore.updateData { moment ->
            moment.toBuilder()
                .setDarkModeSetting(darkModeSetting)
                .build()
        }
    }

    override suspend fun updateBlueFilterEnabled(enabled: Boolean) {
        dataStore.updateData { moment ->
            moment.toBuilder()
                .setBlueLightFilterEnabled(enabled)
                .build()
        }
    }

    override suspend fun updateSoundSetting(soundSetting: SoundSetting) {
        dataStore.updateData { moment ->
            moment.toBuilder()
                .setSoundSetting(soundSetting)
                .build()
        }
    }

    override suspend fun updateBatterySaverEnabled(enabled: Boolean) {
        dataStore.updateData { moment ->
            moment.toBuilder()
                .setBatterySaverEnabled(enabled)
                .build()
        }
    }

    override suspend fun updateReduceBrightnessEnabled(enabled: Boolean) {
        dataStore.updateData { moment ->
            moment.toBuilder()
                .setReduceBrightnessEnabled(enabled)
                .build()
        }
    }

    override suspend fun updateEchoChargeEnabled(enabled: Boolean) {
        dataStore.updateData { moment ->
            moment.toBuilder()
                .setEcoChargeEnabled(enabled)
                .build()
        }
    }

    override suspend fun updateAlwaysOnDisplayEnabled(enabled: Boolean) {
        dataStore.updateData { moment ->
            moment.toBuilder()
                .setAlwaysOnDisplayEnabled(enabled)
                .build()
        }
    }

    override suspend fun updateAirplaneModeEnabled(enabled: Boolean) {
        dataStore.updateData { moment ->
            moment.toBuilder()
                .setAirplaneModeEnabled(enabled)
                .build()
        }
    }
}

