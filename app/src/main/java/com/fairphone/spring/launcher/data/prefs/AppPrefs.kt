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

package com.fairphone.spring.launcher.data.prefs

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

interface AppPrefs {
    suspend fun isFistTimeUse(): Boolean
    suspend fun setFirstTimeUse(value: Boolean)
}

class AppPrefsImpl(private val dataStore: DataStore<Preferences>): AppPrefs {
    companion object {
        val FIRST_TIME_USE = booleanPreferencesKey("first_time_use")
    }

    override suspend fun isFistTimeUse(): Boolean {
        return dataStore.data.map { prefs ->
            prefs[FIRST_TIME_USE] != false
        }.first()
    }

    override suspend fun setFirstTimeUse(value: Boolean) {
        dataStore.edit { prefs ->
            prefs[FIRST_TIME_USE] = value
        }
    }
}
