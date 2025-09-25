/*
 * Copyright (C) 2025 FairPhone B.V.
 *
 * SPDX-FileCopyrightText: 2025. FairPhone B.V.
 *
 * SPDX-License-Identifier: EUPL-1.2
 */

package com.fairphone.spring.launcher.ui.screen.settings.contacts

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fairphone.spring.launcher.data.model.protos.ContactType
import com.fairphone.spring.launcher.domain.usecase.profile.GetEditedProfileUseCase
import com.fairphone.spring.launcher.domain.usecase.profile.UpdateLauncherProfileUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AllowedContactSettingsViewModel(
    context: Context,
    private val getEditedProfileUseCase: GetEditedProfileUseCase,
    private val updateLauncherProfileUseCase: UpdateLauncherProfileUseCase,
) : ViewModel() {

    val screenState: StateFlow<AllowedContactSettingsScreenState> =
        getEditedProfileUseCase.execute(Unit).map { profile ->
            AllowedContactSettingsScreenState.Success(
                activeProfileName = profile.name,
                allowedContactTypeList = getAllowedContactTypeList(context),
                selectedContactType = profile.allowedContacts,
                allowedCustomContactCount = profile.customContactsCount,
                isRepeatCallsEnabled = profile.repeatCallEnabled,
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

    private fun getAllowedContactTypeList(context: Context): List<ContactType> {
        val defaults = mutableListOf(
            ContactType.CONTACT_TYPE_EVERYONE,
            ContactType.CONTACT_TYPE_NONE,
            ContactType.CONTACT_TYPE_ALL_CONTACTS,
            ContactType.CONTACT_TYPE_STARRED,
        )

        // Add custom contact type only if notification access is granted
//        if (context.isNotificationAccessGranted()) {
//            defaults.add(ContactType.CONTACT_TYPE_CUSTOM)
//        }

        return defaults
    }
}

sealed class AllowedContactSettingsScreenState {
    data object Loading : AllowedContactSettingsScreenState()
    data class Success(
        val activeProfileName: String,
        val allowedContactTypeList: List<ContactType>,
        val selectedContactType: ContactType,
        val allowedCustomContactCount: Int,
        val isRepeatCallsEnabled: Boolean,
    ) : AllowedContactSettingsScreenState()
}
