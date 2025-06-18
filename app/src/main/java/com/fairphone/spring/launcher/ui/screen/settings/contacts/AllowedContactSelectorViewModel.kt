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

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fairphone.spring.launcher.data.model.ContactInfo
import com.fairphone.spring.launcher.domain.usecase.contacts.GetAllContactsUseCase
import com.fairphone.spring.launcher.domain.usecase.profile.GetEditedProfileUseCase
import com.fairphone.spring.launcher.domain.usecase.profile.UpdateLauncherProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AllowedContactSelectorViewModel(
    private val getAllContactsUseCase: GetAllContactsUseCase,
    private val getEditedProfileUseCase: GetEditedProfileUseCase,
    private val updateLauncherProfileUseCase: UpdateLauncherProfileUseCase,
) : ViewModel() {

    private val _contactPermissionGranted = MutableStateFlow(false)

    private val _screenState: MutableStateFlow<AllowedContactSelectorScreenState> =
        MutableStateFlow(AllowedContactSelectorScreenState.RequestContactPermission)
    val screenState = _screenState.asStateFlow()

    private val selectedContacts = mutableStateListOf<ContactInfo>()
    private var screenData: ScreenData by mutableStateOf(ScreenData())

    init {
        observePermissionAndLoadContacts()
    }

    private fun observePermissionAndLoadContacts() = viewModelScope.launch {
        _contactPermissionGranted.collectLatest { isGranted ->
            if (isGranted) {
                loadContacts()
            }
        }
    }

    private suspend fun loadContacts() {
        _screenState.update { AllowedContactSelectorScreenState.Loading }

        val editedProfile = getEditedProfileUseCase.execute(Unit).first()
        val contacts = getAllContactsUseCase.execute(Unit).getOrNull() ?: emptyList()

        selectedContacts.clear()
        selectedContacts.addAll(contacts.filter { it.contactUri.toString() in editedProfile.customContactsList })

        screenData = ScreenData(
            allContacts = contacts,
            selectedContacts = selectedContacts.toList(),
            showConfirmButton = false,
        )

        _screenState.update {
            AllowedContactSelectorScreenState.Success(screenData)
        }
    }

    fun onContactPermissionChanged(isGranted: Boolean) {
        _contactPermissionGranted.value = isGranted
    }

    fun onContactSelected(contactInfo: ContactInfo) {
        if (contactInfo in selectedContacts) {
            selectedContacts.remove(contactInfo)
        } else {
            selectedContacts.add(contactInfo)
        }

        updateSuccessState()
    }

    fun onContactDeselected(contactInfo: ContactInfo) {
        selectedContacts.remove(contactInfo)
        updateSuccessState()
    }

    fun confirmContactSelection() = viewModelScope.launch {
        val editedProfile = getEditedProfileUseCase.execute(Unit).first()
        val customContactList = selectedContacts.map { contactInfo ->
            contactInfo.contactUri.toString()
        }
        Log.d("AllowedContact", "Confirming contact selection: $customContactList")
        val newProfile = editedProfile.toBuilder()
            .clearCustomContacts()
            .addAllCustomContacts(customContactList)
            .build()

        val result = updateLauncherProfileUseCase.execute(newProfile)
        _screenState.update {
            if (result.isSuccess) {
                AllowedContactSelectorScreenState.UpdateContactSelectionSuccess
            } else {
                AllowedContactSelectorScreenState.UpdateContactSelectionFailure
            }
        }
    }

    private fun updateSuccessState() {
        _screenState.update {
            AllowedContactSelectorScreenState.Success(
                screenData = screenData.copy(
                    selectedContacts = selectedContacts.toList(),
                    showConfirmButton = selectedContacts.isNotEmpty()
                )
            )
        }
    }
}

sealed class AllowedContactSelectorScreenState {
    data object RequestContactPermission : AllowedContactSelectorScreenState()
    data object Loading : AllowedContactSelectorScreenState()
    data class Success(val screenData: ScreenData) : AllowedContactSelectorScreenState()
    data object UpdateContactSelectionSuccess : AllowedContactSelectorScreenState()
    data object UpdateContactSelectionFailure : AllowedContactSelectorScreenState()
}

data class ScreenData(
    val allContacts: List<ContactInfo> = emptyList(),
    val selectedContacts: List<ContactInfo> = emptyList(),
    val showConfirmButton: Boolean = false,
)

