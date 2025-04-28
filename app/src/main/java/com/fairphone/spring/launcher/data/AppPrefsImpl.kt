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

package com.fairphone.spring.launcher.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.fairphone.spring.launcher.di.appPrefsDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

interface AppPrefs {
    suspend fun isFistTimeUse(): Boolean
    suspend fun setFirstTimeUse(value: Boolean)
}

class AppPrefsImpl(context: Context): AppPrefs {
    companion object {
        val FIRST_TIME_USE = booleanPreferencesKey("first_time_use")
    }

    private val dataStore = context.appPrefsDataStore

    override suspend fun isFistTimeUse(): Boolean {
        return dataStore.data.map { preferences ->
            preferences[FIRST_TIME_USE] != false
        }.first()
    }

    override suspend fun setFirstTimeUse(value: Boolean) {
        dataStore.edit { preferences ->
            preferences[FIRST_TIME_USE] = value
        }
    }
}
