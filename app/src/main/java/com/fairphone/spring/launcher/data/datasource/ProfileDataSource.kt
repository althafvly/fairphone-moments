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
import com.fairphone.spring.launcher.data.model.LauncherProfile
import com.fairphone.spring.launcher.data.model.LauncherProfiles
import com.fairphone.spring.launcher.data.model.launcherProfiles
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import java.io.IOException

interface ProfileDataSource {
    fun getActiveProfile(): Flow<LauncherProfile>
    fun getEditedProfile(): Flow<LauncherProfile>
    fun getProfiles(): Flow<List<LauncherProfile>>

    suspend fun setActiveProfile(profileId: String)
    suspend fun setEditedProfile(profileId: String)
    suspend fun createLauncherProfile(profile: LauncherProfile)
    suspend fun deleteLauncherProfile(profile: LauncherProfile)
    suspend fun updateLauncherProfile(profile: LauncherProfile)
    suspend fun updateVisibleApps(profileId: String, visibleApps: List<String>)
}

class ProfileDataSourceImpl(private val dataStore: DataStore<LauncherProfiles>) :
    ProfileDataSource {

    override fun getActiveProfile(): Flow<LauncherProfile> =
        getLauncherProfiles().transform { profile ->
            if (profile.profilesList.isNotEmpty()) {
                // We only emit the profile when the list is populated
                emit(profile.profilesList.first { it.id == profile.active })
            }
        }

    override fun getEditedProfile(): Flow<LauncherProfile> =
        getLauncherProfiles().transform { profile ->
            if (profile.profilesList.isNotEmpty()) {
                // We only emit the profile when the list is populated
                emit(
                    profile.profilesList.firstOrNull { it.id == profile.edited }
                    ?: profile.profilesList.first { it.id == profile.active }
                )
            }
        }

    override fun getProfiles(): Flow<List<LauncherProfile>> =
        getLauncherProfiles().map { it.profilesList }

    private fun getLauncherProfiles(): Flow<LauncherProfiles> {
        return dataStore.data.catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                Log.e("ProfileDataSource", "Error reading sort order preferences.", exception)
                emit(LauncherProfiles.getDefaultInstance())
            } else {
                throw exception
            }
        }
    }

    override suspend fun setActiveProfile(profileId: String) {
        dataStore.updateData { profiles ->
            profiles
                .toBuilder()
                .setActive(profileId)
                .build()
        }
    }

    override suspend fun setEditedProfile(profileId: String) {
        dataStore.updateData { profiles ->
            profiles
                .toBuilder()
                .setEdited(profileId)
                .build()
        }
    }

    override suspend fun createLauncherProfile(profile: LauncherProfile) {
        val existing = getLauncherProfiles().firstOrNull()
        if (existing == null || existing.profilesList.isEmpty()) {
            dataStore.updateData {
                launcherProfiles {
                    profiles.add(profile)
                    active = profile.id
                }
            }
        } else {
            if (existing.profilesList.map { it.id }.contains(profile.id)) {
                // In a perfect world we must never create a profile with the same id. If we
                // try we update the existing profile
                updateLauncherProfile(profile)
            } else {
                dataStore.updateData { profiles ->
                    profiles
                        .toBuilder()
                        .clearProfiles()
                        .addAllProfiles((existing.profilesList + listOf(profile)).toMutableList())
                        .build()
                }
            }
        }
    }

    override suspend fun deleteLauncherProfile(profile: LauncherProfile) {
        dataStore.updateData { profiles ->
            val newProfiles = getProfiles().first().filter { it.id != profile.id }.toMutableList()
            profiles
                .toBuilder()
                .clearProfiles()
                .addAllProfiles(newProfiles)
                .build()
        }
    }

    override suspend fun updateLauncherProfile(profile: LauncherProfile) {
        dataStore.updateData { profiles ->
            profiles
                .toBuilder()
                .clearProfiles()
                .addAllProfiles(updateLauncherProfileInList(profile.id) {
                    profile
                })
                .build()
        }
    }

    override suspend fun updateVisibleApps(profileId: String, visibleApps: List<String>) {
        dataStore.updateData { profiles ->
            profiles
                .toBuilder()
                .clearProfiles()
                .addAllProfiles(updateLauncherProfileInList(profileId) {
                    it.toBuilder()
                        .clearVisibleApps()
                        .addAllVisibleApps(visibleApps)
                        .build()
                })
                .build()
        }
    }

    /**
     * In this case we have at least one element in the list and the list must contain the
     * given profile id
     */
    private suspend fun updateLauncherProfileInList(
        profileId: String,
        profileUpdateAction: (LauncherProfile) -> LauncherProfile
    ): List<LauncherProfile> =
        getProfiles().first().toMutableList().also {
            it.replaceAll {
                // We update only the given profile id inthe list
                if (profileId == it.id) profileUpdateAction(it) else it
            }
        }

}

