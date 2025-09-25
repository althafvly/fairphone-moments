/*
 * Copyright (C) 2025 FairPhone B.V.
 *
 * SPDX-FileCopyrightText: 2025. FairPhone B.V.
 *
 * SPDX-License-Identifier: EUPL-1.2
 */

package com.fairphone.spring.launcher.data.repository

import com.fairphone.spring.launcher.data.datasource.ProfileDataSource
import com.fairphone.spring.launcher.data.model.protos.LauncherProfile
import com.fairphone.spring.launcher.data.model.protos.LauncherProfileApp
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
    suspend fun deleteProfile(profile: LauncherProfile)
    suspend fun updateProfile(profile: LauncherProfile)
    suspend fun updateVisibleApps(profileId: String, visibleApps: List<LauncherProfileApp>)
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

    override suspend fun deleteProfile(profile: LauncherProfile) =
        dataSource.deleteLauncherProfile(profile)

    override suspend fun updateProfile(profile: LauncherProfile) =
        dataSource.updateLauncherProfile(profile)

    override suspend fun updateVisibleApps(profileId: String, visibleApps: List<LauncherProfileApp>) =
        dataSource.updateVisibleApps(profileId, visibleApps)
}
