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

import com.fairphone.spring.launcher.data.datasource.ProfileDataSource
import com.fairphone.spring.launcher.data.model.LauncherProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface LauncherProfileRepository {
    fun getActiveProfile(): Flow<LauncherProfile>
    fun getEditedProfile(): Flow<LauncherProfile>
    fun getProfiles(): Flow<List<LauncherProfile>>
    fun getProfile(profileId: String): Flow<LauncherProfile>

    suspend fun setActiveProfile(profileId: String)
    suspend fun setEditedProfile(profileId: String)
    suspend fun createProfile(profile: LauncherProfile)
    suspend fun removeProfile(profile: LauncherProfile)
    suspend fun updateProfile(profile: LauncherProfile)
    suspend fun updateVisibleApps(profileId: String, visibleApps: List<String>)
}

class LauncherProfileRepositoryImpl(private val dataSource: ProfileDataSource) :
    LauncherProfileRepository {

    override fun getActiveProfile(): Flow<LauncherProfile> =
        dataSource.getActiveProfile()

    override fun getEditedProfile(): Flow<LauncherProfile> =
        dataSource.getEditedProfile()

    override fun getProfiles(): Flow<List<LauncherProfile>> =
        dataSource.getProfiles()

    override fun getProfile(profileId: String): Flow<LauncherProfile> =
        dataSource.getProfiles().map { it.first { it.id == profileId } }

    override suspend fun setActiveProfile(profileId: String) =
        dataSource.setActiveProfile(profileId)

    override suspend fun setEditedProfile(profileId: String) =
        dataSource.setEditedProfile(profileId)

    override suspend fun createProfile(profile: LauncherProfile) =
        dataSource.createLauncherProfile(profile)

    override suspend fun removeProfile(profile: LauncherProfile) =
        dataSource.removeLauncherProfile(profile)

    override suspend fun updateProfile(profile: LauncherProfile) =
        dataSource.updateLauncherProfile(profile)

    override suspend fun updateVisibleApps(profileId: String, visibleApps: List<String>) =
        dataSource.updateVisibleApps(profileId, visibleApps)
}
