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

import android.util.Log
import androidx.datastore.core.DataStore
import com.fairphone.spring.launcher.data.model.ContactType
import com.fairphone.spring.launcher.data.model.LauncherProfile
import com.fairphone.spring.launcher.data.model.SoundSetting
import com.fairphone.spring.launcher.data.model.UiMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.io.IOException

interface ProfileDataSource {
    fun getActiveProfile(): Flow<LauncherProfile>
    suspend fun updateLauncherProfile(profile: LauncherProfile)
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

class ProfileDataSourceImpl(private val dataStore: DataStore<LauncherProfile>) : ProfileDataSource {


    override fun getActiveProfile(): Flow<LauncherProfile> {
        return dataStore.data
            .catch { exception ->
                // dataStore.data throws an IOException when an error is encountered when reading data
                if (exception is IOException) {
                    Log.e("ProfileDataSource", "Error reading sort order preferences.", exception)
                    emit(LauncherProfile.getDefaultInstance())
                } else {
                    throw exception
                }
            }
    }

    override suspend fun updateLauncherProfile(profile: LauncherProfile) {
        dataStore.updateData { profile }
    }

    override suspend fun updateVisibleApps(visibleApps: List<String>) {
        dataStore.updateData { profile ->
            profile.toBuilder()
                .clearVisibleApps()
                .addAllVisibleApps(visibleApps)
                .build()
        }
    }

    override suspend fun updateName(name: String) {
        dataStore.updateData { profile ->
            profile.toBuilder()
                .setName(name)
                .build()
        }
    }

    override suspend fun updateAllowedContacts(allowedContacts: ContactType) {
        dataStore.updateData { profile ->
            profile.toBuilder()
                .setAllowedContact(allowedContacts)
                .build()
        }
    }

    override suspend fun updateCustomContacts(customContacts: List<String>) {
        dataStore.updateData { profile ->
            profile.toBuilder()
                .clearCustomContacts()
                .addAllCustomContacts(customContacts)
                .build()
        }
    }

    override suspend fun updateRepeatCallEnabled(enabled: Boolean) {
        dataStore.updateData { profile ->
            profile.toBuilder()
                .setRepeatCallEnabled(enabled)
                .build()
        }

    }

    override suspend fun updateAppNotifications(appNotifications: List<String>) {
        dataStore.updateData { profile ->
            profile.toBuilder()
                .clearAppNotifications()
                .addAllAppNotifications(appNotifications)
                .build()
        }
    }

    override suspend fun updateWallpaperId(wallpaperId: Int) {
        dataStore.updateData { profile ->
            profile.toBuilder()
                .setWallpaperId(wallpaperId)
                .build()
        }
    }

    override suspend fun updateDarkModeSetting(uiMode: UiMode) {
        dataStore.updateData { profile ->
            profile.toBuilder()
                .setUiMode(uiMode)
                .build()
        }
    }

    override suspend fun updateBlueFilterEnabled(enabled: Boolean) {
        dataStore.updateData { profile ->
            profile.toBuilder()
                .setBlueLightFilterEnabled(enabled)
                .build()
        }
    }

    override suspend fun updateSoundSetting(soundSetting: SoundSetting) {
        dataStore.updateData { profile ->
            profile.toBuilder()
                .setSoundSetting(soundSetting)
                .build()
        }
    }

    override suspend fun updateBatterySaverEnabled(enabled: Boolean) {
        dataStore.updateData { profile ->
            profile.toBuilder()
                .setBatterySaverEnabled(enabled)
                .build()
        }
    }

    override suspend fun updateReduceBrightnessEnabled(enabled: Boolean) {
        dataStore.updateData { profile ->
            profile.toBuilder()
                .setReduceBrightnessEnabled(enabled)
                .build()
        }
    }

    override suspend fun updateEchoChargeEnabled(enabled: Boolean) {
        dataStore.updateData { profile ->
            profile.toBuilder()
                .setEcoChargeEnabled(enabled)
                .build()
        }
    }

    override suspend fun updateAlwaysOnDisplayEnabled(enabled: Boolean) {
        dataStore.updateData { profile ->
            profile.toBuilder()
                .setAlwaysOnDisplayEnabled(enabled)
                .build()
        }
    }

    override suspend fun updateAirplaneModeEnabled(enabled: Boolean) {
        dataStore.updateData { profile ->
            profile.toBuilder()
                .setAirplaneModeEnabled(enabled)
                .build()
        }
    }
}

