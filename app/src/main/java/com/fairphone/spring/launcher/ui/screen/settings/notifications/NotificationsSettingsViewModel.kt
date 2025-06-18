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

package com.fairphone.spring.launcher.ui.screen.settings.notifications

import android.Manifest
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fairphone.spring.launcher.R
import com.fairphone.spring.launcher.domain.usecase.profile.GetEditedProfileUseCase
import com.fairphone.spring.launcher.domain.usecase.profile.UpdateLauncherProfileUseCase
import com.fairphone.spring.launcher.util.isNotificationAccessGranted
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NotificationsSettingsViewModel(
    context: Context,
    private val getEditedProfileUseCase: GetEditedProfileUseCase,
    private val updateLauncherProfileUseCase: UpdateLauncherProfileUseCase,
) : ViewModel() {

    val screenState: StateFlow<AllowedNotificationsSettingsScreenState> =
        getEditedProfileUseCase.execute(Unit)
            .map { profile ->
                AllowedNotificationsSettingsScreenState.Success(
                    AllowedNotificationSettingsData(
                        isRepeatCallsEnabled = profile.repeatCallEnabled,
                        notificationPermissions = getPermissions(context),
                    )
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = AllowedNotificationsSettingsScreenState.Loading
            )

    /**
     * Used when the user use the switch button to allow/disallow repeat calls
     */
    fun onAllowRepeatCalls(allow: Boolean) = viewModelScope.launch {
        val profile = getEditedProfileUseCase.execute(Unit).first()
        updateLauncherProfileUseCase.execute(
            profile.toBuilder().setRepeatCallEnabled(allow).build()
        )
    }

    private fun getPermissions(context: Context): List<PermissionSetting> {
        return listOf(
            PermissionSetting(
                permissionName = Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE,
                titleResId = R.string.permission_toggle_title_notification_access,
                subtitleResId = R.string.permission_toggle_subtitle_notification_access,
                isGranted = context.isNotificationAccessGranted()
            ),
        )
    }
}

sealed class AllowedNotificationsSettingsScreenState() {
    data object Loading : AllowedNotificationsSettingsScreenState()
    data class Success(
        val data: AllowedNotificationSettingsData
    ) : AllowedNotificationsSettingsScreenState()

}

data class AllowedNotificationSettingsData(
    val isRepeatCallsEnabled: Boolean,
    val notificationPermissions: List<PermissionSetting>,
)

data class PermissionSetting(
    val permissionName: String,
    val titleResId: Int,
    val subtitleResId: Int,
    val isGranted: Boolean,
)