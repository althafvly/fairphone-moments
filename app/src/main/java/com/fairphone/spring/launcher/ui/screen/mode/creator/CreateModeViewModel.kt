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

package com.fairphone.spring.launcher.ui.screen.mode.creator

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fairphone.spring.launcher.data.model.CreateLauncherProfile
import com.fairphone.spring.launcher.domain.usecase.profile.CreateLauncherProfileUseCase
import com.fairphone.spring.launcher.domain.usecase.profile.SetEditedProfileUseCase
import kotlinx.coroutines.launch

/**
 * [VisibleAppSelectorViewModel] is used to edit an existing profile, but this one is used
 * on the creation wizard when the profile is not yet created
 */
class CreateModeViewModel(
    private val createLauncherProfileUseCase: CreateLauncherProfileUseCase,
    private val setEditedProfileUseCase: SetEditedProfileUseCase,
) : ViewModel() {

    fun save(params: CreateLauncherProfile) {
        viewModelScope.launch {
            val result = createLauncherProfileUseCase.execute(params)
            setEditedProfileUseCase.execute(params.id)

            // TODO do we need more ?
            if (result.isFailure) {
                Log.e("App", "Failed to create initial profile", result.exceptionOrNull())
            }
        }
    }
}
