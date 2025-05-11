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
import com.fairphone.spring.launcher.domain.usecase.profile.GetActiveProfileUseCase
import com.fairphone.spring.launcher.domain.usecase.profile.GetAllProfilesUseCase
import com.fairphone.spring.launcher.domain.usecase.profile.SetActiveProfileUseCase
import com.fairphone.spring.launcher.domain.usecase.profile.SetEditedProfileUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch

class ModeSwitcherViewModel(
    private val getActiveProfileUseCase: GetActiveProfileUseCase,
    private val getAllProfilesUseCase: GetAllProfilesUseCase,
    private val setActiveProfileUseCase: SetActiveProfileUseCase,
    private val setEditedProfileUseCase: SetEditedProfileUseCase,
) : ViewModel() {

    val screenState: StateFlow<ModeSwitcherScreenState?> =
        getAllProfilesUseCase.execute(Unit)
            .zip(getActiveProfileUseCase.execute(Unit)) { profiles, active ->
                ModeSwitcherScreenState(
                    activeProfile = active,
                    profiles = profiles
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = null,
            )

    /**
     * TODO: Add javadoc
     */
    fun updateActiveProfile(profile: LauncherProfile) {
        viewModelScope.launch {
            setActiveProfileUseCase.execute(profile.id)
        }
    }

    /**
     * TODO: Add javadoc
     */
    suspend fun editActiveProfileSettings(profile: LauncherProfile): Result<Unit> {
        return setEditedProfileUseCase.execute(profile.id)
    }
}

data class ModeSwitcherScreenState(
    val activeProfile: LauncherProfile,
    val profiles: List<LauncherProfile>,
)
