/*
 * Copyright (C) 2025 FairPhone B.V.
 *
 * SPDX-FileCopyrightText: 2025. FairPhone B.V.
 *
 * SPDX-License-Identifier: EUPL-1.2
 */

package com.fairphone.spring.launcher.ui.screen.settings.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fairphone.spring.launcher.data.model.AppInfo
import com.fairphone.spring.launcher.data.model.protos.LauncherProfile
import com.fairphone.spring.launcher.data.model.protos.LauncherProfileApp
import com.fairphone.spring.launcher.data.model.protos.copy
import com.fairphone.spring.launcher.data.repository.AppInfoRepository
import com.fairphone.spring.launcher.domain.usecase.profile.DeleteLauncherProfileUseCase
import com.fairphone.spring.launcher.domain.usecase.profile.GetActiveProfileUseCase
import com.fairphone.spring.launcher.domain.usecase.profile.GetAllProfilesUseCase
import com.fairphone.spring.launcher.domain.usecase.profile.GetEditedProfileUseCase
import com.fairphone.spring.launcher.domain.usecase.profile.SetActiveProfileUseCase
import com.fairphone.spring.launcher.domain.usecase.profile.UpdateLauncherProfileUseCase
import com.fairphone.spring.launcher.ui.icons.mode.ModeIcon
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch

class ProfileSettingsViewModel(
    context: Context,
    private val appInfoRepository: AppInfoRepository,
    private val getEditedProfileUseCase: GetEditedProfileUseCase,
    private val getActiveProfileUseCase: GetActiveProfileUseCase,
    private val setActiveProfileUseCase: SetActiveProfileUseCase,
    private val getAllProfilesUseCase: GetAllProfilesUseCase,
    private val updateLauncherProfileUseCase: UpdateLauncherProfileUseCase,
    private val deleteLauncherProfileUseCase: DeleteLauncherProfileUseCase
) : ViewModel() {

    val screenState: StateFlow<ProfileSettingsScreenState> =
        getAllProfilesUseCase.execute(Unit)
            .zip(getEditedProfileUseCase.execute(Unit)) { profiles, editedProfile -> Pair(profiles, editedProfile)}.map { combined ->
                val visibleApps = getAppInfoList(context, combined.second.launcherProfileAppsList)
                val activeProfile = getActiveProfileUseCase.execute(Unit).first()
                ProfileSettingsScreenState.Success(
                    profile = combined.second,
                    visibleApps = visibleApps,
                    hasMultipleProfiles = combined.first.size > 1,
                    isActiveProfile = combined.second.id == activeProfile.id,
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = ProfileSettingsScreenState.Loading
            )


    private fun getAppInfoList(context: Context, appIds: List<LauncherProfileApp>): List<AppInfo> {
        return appInfoRepository.getAppInfosByProfileApps(context, appIds)
    }

    fun updateProfileName(name: String) = viewModelScope.launch {
        val editedProfile = getEditedProfileUseCase.execute(Unit).first()
        val updatedProfile = editedProfile.toBuilder().setName(name.trim()).build()
        updateLauncherProfileUseCase.execute(updatedProfile)
    }

    fun updateProfileIcon() = viewModelScope.launch {
        when (screenState.first()) {
            is ProfileSettingsScreenState.Loading -> {}
            is ProfileSettingsScreenState.Success -> {
                val element = screenState.first() as ProfileSettingsScreenState.Success
                val updatedProfile = element.profile.copy {
                    icon = ModeIcon.nextIcon(element.profile.icon).name
                }
                updateLauncherProfileUseCase.execute(updatedProfile)
            }
        }

    }

    fun setEditedProfileAsActive() = viewModelScope.launch {
        val editedProfile = getEditedProfileUseCase.execute(Unit).first()
        setActiveProfileUseCase.execute(editedProfile.id)
    }

    fun deleteProfile() = viewModelScope.launch {
        val editedProfile = getEditedProfileUseCase.execute(Unit).first()
        deleteLauncherProfileUseCase.execute(editedProfile)
    }
}

sealed interface ProfileSettingsScreenState {
    object Loading : ProfileSettingsScreenState
    data class Success(
        val profile: LauncherProfile,
        val visibleApps: List<AppInfo>,
        val hasMultipleProfiles: Boolean,
        val isActiveProfile: Boolean,
    ) : ProfileSettingsScreenState
}
