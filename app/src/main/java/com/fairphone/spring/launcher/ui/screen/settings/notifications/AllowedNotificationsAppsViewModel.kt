/*
 * Copyright (C) 2025 FairPhone B.V.
 *
 * SPDX-FileCopyrightText: 2025. FairPhone B.V.
 *
 * SPDX-License-Identifier: EUPL-1.2
 */

package com.fairphone.spring.launcher.ui.screen.settings.notifications

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fairphone.spring.launcher.data.model.AppInfo
import com.fairphone.spring.launcher.data.repository.AppInfoRepository
import com.fairphone.spring.launcher.domain.usecase.profile.GetEditedProfileUseCase
import com.fairphone.spring.launcher.domain.usecase.profile.UpdateLauncherProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AllowedNotificationsAppsViewModel(
    context: Application,
    appInfoRepository: AppInfoRepository,
    private val getEditedProfileUseCase: GetEditedProfileUseCase,
    private val updateLauncherProfileUseCase: UpdateLauncherProfileUseCase,
) : ViewModel() {

    private val _screenState: MutableStateFlow<AllowedNotificationsAppsScreenState> =
        MutableStateFlow(AllowedNotificationsAppsScreenState.Loading)
    val screenState = _screenState.asStateFlow()

    private var installedApps = mutableStateListOf<AppInfo>()
    private var notificationApps = mutableStateListOf<AppInfo>()

    init {
        viewModelScope.launch {
            installedApps.addAll(appInfoRepository.getAllInstalledApps(context))
            val currentProfile = getEditedProfileUseCase.execute(Unit).first()
            notificationApps.addAll(
                appInfoRepository.getAppInfosByPackageNames(
                    context,
                    currentProfile.appNotificationsList
                )
            )

            _screenState.update {
                AllowedNotificationsAppsScreenState.Success(
                    AllowedNotificationsAppsScreenData(
                        profileId = currentProfile.id,
                        allNotificationApps = installedApps,
                        allowedNotificationApps = notificationApps,
                        repeatCallEnabled = currentProfile.repeatCallEnabled,
                    )
                )
            }
        }
    }

    /**
     * Used when the user want to allow/disallow app notifications
     */
    fun updateAppNotificationRight(appId: String, value: Boolean) =
        viewModelScope.launch {
            val profile = getEditedProfileUseCase.execute(Unit).first()
            val appNotifications = if (value) {
                profile.appNotificationsList.plusElement(appId).distinct()
            } else {
                profile.appNotificationsList.minusElement(appId)
            }

            updateLauncherProfileUseCase.execute(
                profile.toBuilder()
                    .clearAppNotifications()
                    .addAllAppNotifications(appNotifications)
                    .build()
            )
            _screenState.updateState {
                it.copy(allowedNotificationApps = installedApps.filter { app ->
                    appNotifications.contains(app.id)
                })
            }
        }
}

fun MutableStateFlow<AllowedNotificationsAppsScreenState>.updateState(
    screenDataModifier: (AllowedNotificationsAppsScreenData) -> AllowedNotificationsAppsScreenData
) {
    if (value is AllowedNotificationsAppsScreenState.Success) {
        update {
            AllowedNotificationsAppsScreenState.Success(
                screenDataModifier.invoke(
                    (value as AllowedNotificationsAppsScreenState.Success).data
                )
            )
        }
    }
}

sealed class AllowedNotificationsAppsScreenState {
    data object Loading : AllowedNotificationsAppsScreenState()
    data class Success(val data: AllowedNotificationsAppsScreenData) :
        AllowedNotificationsAppsScreenState()
}

data class AllowedNotificationsAppsScreenData(
    val profileId: String,
    val allNotificationApps: List<AppInfo>,
    val allowedNotificationApps: List<AppInfo>,
    val repeatCallEnabled: Boolean,
)