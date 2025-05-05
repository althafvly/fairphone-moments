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

package com.fairphone.spring.launcher.ui.screen.settings.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fairphone.spring.launcher.data.model.AppInfo
import com.fairphone.spring.launcher.data.model.LauncherProfile
import com.fairphone.spring.launcher.data.repository.AppInfoRepository
import com.fairphone.spring.launcher.data.repository.LauncherProfileRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileSettingsViewModel(
    context: Context,
    private val appInfoRepository: AppInfoRepository,
    private val launcherProfileRepository: LauncherProfileRepository,
) : ViewModel() {

    val screenState: StateFlow<ProfileSettingsScreenState?> = launcherProfileRepository.getActiveProfile()
        .map { profile ->
            val visibleApps = appInfoRepository.getAppInfos(context, profile.visibleAppsList)
            ProfileSettingsScreenState(
                profile = profile,
                visibleApps = visibleApps
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null
        )

    fun updateProfileName(name: String) {
        viewModelScope.launch {
            launcherProfileRepository.updateName(name)
        }
    }
}

data class ProfileSettingsScreenState(
    val profile: LauncherProfile,
    val visibleApps: List<AppInfo> = emptyList(),
)
