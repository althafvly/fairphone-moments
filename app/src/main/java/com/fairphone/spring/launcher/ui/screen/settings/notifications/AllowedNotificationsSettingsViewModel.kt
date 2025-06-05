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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fairphone.spring.launcher.domain.usecase.profile.GetEditedProfileUseCase
import com.fairphone.spring.launcher.domain.usecase.profile.UpdateLauncherProfileUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AllowedNotificationsSettingsViewModel(
    private val getEditedProfileUseCase: GetEditedProfileUseCase,
    private val updateLauncherProfileUseCase: UpdateLauncherProfileUseCase,
) : ViewModel() {

    val screenState: StateFlow<AllowedNotificationsSettingsScreenState> =
        getEditedProfileUseCase.execute(Unit)
            .map { profile ->
                AllowedNotificationsSettingsScreenState.Success(
                    AllowedNotificationSettingsData(
                        allowedNotificationAppsCount = profile.appNotificationsList.size,
                        repeatCallEnabled = profile.repeatCallEnabled,
                    )
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = AllowedNotificationsSettingsScreenState.Loading
            )

    /**
     * Used when the user use the switch button to allow/disallow repeat calls
     */
    fun updateRepeatCallEnabledIndicator(indicator: Boolean) =
        viewModelScope.launch {
            val profile = getEditedProfileUseCase.execute(Unit).first()
            updateLauncherProfileUseCase.execute(
                profile.toBuilder().setRepeatCallEnabled(indicator).build()
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
    val allowedNotificationAppsCount: Int,
    val repeatCallEnabled: Boolean,
)