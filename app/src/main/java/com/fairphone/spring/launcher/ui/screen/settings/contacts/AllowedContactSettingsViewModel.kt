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

package com.fairphone.spring.launcher.ui.screen.settings.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fairphone.spring.launcher.data.model.ContactType
import com.fairphone.spring.launcher.data.model.Defaults
import com.fairphone.spring.launcher.domain.usecase.profile.GetEditedProfileUseCase
import com.fairphone.spring.launcher.domain.usecase.profile.UpdateLauncherProfileUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AllowedContactSettingsViewModel(
    private val getEditedProfileUseCase: GetEditedProfileUseCase,
    private val updateLauncherProfileUseCase: UpdateLauncherProfileUseCase,
) : ViewModel() {

    val screenState: StateFlow<AllowedContactSettingsScreenState> =
        getEditedProfileUseCase.execute(Unit).map { profile ->
            AllowedContactSettingsScreenState.Success(
                activeProfileName = profile.name,
                allowedContactTypeList = Defaults.CONTACT_TYPE_LIST,
                selectedContactType = profile.allowedContacts,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = AllowedContactSettingsScreenState.Loading
        )

    fun onContactTypeSelected(peopleType: ContactType) = viewModelScope.launch {
        val profile = getEditedProfileUseCase.execute(Unit).first()
        val updatedProfile = profile.toBuilder().setAllowedContacts(peopleType).build()
        updateLauncherProfileUseCase.execute(updatedProfile)
    }
}

sealed class AllowedContactSettingsScreenState {
    data object Loading : AllowedContactSettingsScreenState()
    data class Success(
        val activeProfileName: String,
        val allowedContactTypeList: List<ContactType>,
        val selectedContactType: ContactType,
    ) : AllowedContactSettingsScreenState()
}
