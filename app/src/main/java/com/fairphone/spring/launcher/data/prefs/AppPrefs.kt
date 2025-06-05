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
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

enum class UsageMode {
    /**
     * When a user turns on the default Moment for the first time an extra button is shown
     * on the home and the user can update the settings.
     */
    ON_BOARDING,

    /**
     * When the onboarding is complete or when the user chooses to stop this process we use
     * this usage to display a tooltip on the home page
     */
    ON_BOARDING_COMPLETE,

    /**
     * This is the default mode when the user has decided to not display anymore the tooltip
     * on the home
     */
    DEFAULT
}

interface AppPrefs {
    suspend fun isFirstTimeUse(): Boolean
    suspend fun setFirstTimeUse(value: Boolean)
    suspend fun usageMode(): UsageMode
    suspend fun setUsageMode(usageMode: UsageMode)
}

class AppPrefsImpl(private val dataStore: DataStore<Preferences>) : AppPrefs {
    companion object {
        val FIRST_TIME_USE = booleanPreferencesKey("first_time_use")
        val ONBOARDING_COMPLETION = stringPreferencesKey("onboarding_complete")
    }

    override suspend fun isFirstTimeUse(): Boolean {
        return dataStore.data.map { prefs ->
            prefs[FIRST_TIME_USE] != false
        }.first()
    }

    override suspend fun setFirstTimeUse(value: Boolean) {
        dataStore.edit { prefs ->
            prefs[FIRST_TIME_USE] = value
        }
    }

    override suspend fun usageMode(): UsageMode =
        dataStore.data
            .map { prefs ->
                UsageMode.valueOf(
                    prefs[ONBOARDING_COMPLETION] ?: UsageMode.ON_BOARDING.name
                )
            }
            .first()

    override suspend fun setUsageMode(usageMode: UsageMode) {
        dataStore.edit { prefs ->
            prefs[ONBOARDING_COMPLETION] = usageMode.name
        }
    }
}
