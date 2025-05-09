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

package com.fairphone.spring.launcher.ui.screen.mode

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fairphone.spring.launcher.data.model.LauncherProfile
import com.fairphone.spring.launcher.data.repository.LauncherProfileRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.zip

class ModeSwitcherViewModel(launcherProfileRepository: LauncherProfileRepository) : ViewModel() {

    val screenState: StateFlow<ModeSwitcherScreenState?> =
        launcherProfileRepository
            .getProfiles()
            .zip(launcherProfileRepository.getActiveProfile()) { profiles, active ->
                ModeSwitcherScreenState(
                    activeProfile = active,
                    profiles = profiles
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = null,
            )
}

data class ModeSwitcherScreenState(
    val activeProfile: LauncherProfile,
    val profiles: List<LauncherProfile>,
)
