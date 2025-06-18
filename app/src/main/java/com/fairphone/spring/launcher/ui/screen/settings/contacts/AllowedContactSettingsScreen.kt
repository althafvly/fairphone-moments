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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fairphone.spring.launcher.R
import com.fairphone.spring.launcher.data.model.protos.ContactType
import com.fairphone.spring.launcher.ui.component.RadioButtonListItem
import com.fairphone.spring.launcher.ui.theme.FairphoneTypography
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme

@Composable
fun AllowedContactSettingsScreen(
    screenState: AllowedContactSettingsScreenState,
    onContactTypeSelected: (ContactType) -> Unit,
) {
    when (screenState) {
        is AllowedContactSettingsScreenState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize()
            )
        }

        is AllowedContactSettingsScreenState.Success -> {
            AllowedContactSettingsScreen(
                profileName = screenState.activeProfileName,
                allowedContactTypeList = screenState.allowedContactTypeList,
                selectedContactType = screenState.selectedContactType,
                allowedCustomContactCount = screenState.allowedCustomContactCount,
                onContactTypeSelected = onContactTypeSelected,
            )
        }
    }
}

@Composable
fun AllowedContactSettingsScreen(
    profileName: String,
    allowedContactTypeList: List<ContactType>,
    selectedContactType: ContactType,
    allowedCustomContactCount: Int,
    onContactTypeSelected: (ContactType) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = stringResource(R.string.setting_header_allowed_contacts, profileName),
            style = FairphoneTypography.BodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(start = 36.dp, end = 36.dp, top = 24.dp)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
        ) {
            allowedContactTypeList.forEach { peopleType ->
                val isSelected = peopleType == selectedContactType
                RadioButtonListItem(
                    title = stringResource(peopleType.getNameResId()),
                    isSelected = isSelected,
                    onClick = { onContactTypeSelected(peopleType) },
                    subtitle = when (peopleType) {
                        ContactType.CONTACT_TYPE_CUSTOM -> {
                            if (isSelected) {
                                customAllowedContactsSubtitle(allowedCustomContactCount)
                            } else null
                        }

                        else -> null
                    },
                    leadingIcon = when (peopleType) {
                        ContactType.CONTACT_TYPE_CUSTOM -> Icons.AutoMirrored.Filled.KeyboardArrowRight
                        else -> null
                    },
                )
            }
        }
    }
}

@Composable
fun customAllowedContactsSubtitle(count: Int) =
    if (count == 0) {
        stringResource(R.string.setting_notifications_none)
    } else {
        pluralStringResource(R.plurals.setting_notifications_nb_allowed, count, count)
    }

@Composable
@Preview
fun AllowedContactSettingsScreen_Preview() {
    SpringLauncherTheme {
        AllowedContactSettingsScreen(
            profileName = "Essentials",
            allowedContactTypeList = listOf(
                ContactType.CONTACT_TYPE_EVERYONE,
                ContactType.CONTACT_TYPE_NONE,
                ContactType.CONTACT_TYPE_ALL_CONTACTS,
                ContactType.CONTACT_TYPE_STARRED,
                ContactType.CONTACT_TYPE_CUSTOM,
            ),
            selectedContactType = ContactType.CONTACT_TYPE_CUSTOM,
            allowedCustomContactCount = 3,
            onContactTypeSelected = {},
        )
    }
}

@Composable
@Preview(uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
fun AllowedContactSettingsScreen_DarkPreview() {
    AllowedContactSettingsScreen_Preview()
}


fun ContactType.getNameResId(): Int = when (this) {
    ContactType.CONTACT_TYPE_EVERYONE -> R.string.people_type_everyone
    ContactType.CONTACT_TYPE_NONE -> R.string.people_type_none
    ContactType.CONTACT_TYPE_ALL_CONTACTS -> R.string.people_type_all_contacts
    ContactType.CONTACT_TYPE_STARRED -> R.string.people_type_starred
    ContactType.CONTACT_TYPE_CUSTOM -> R.string.people_type_custom
    ContactType.UNRECOGNIZED -> R.string.people_type_none // fallback to 'none'
}

@Composable
fun allowedContactSubtitle(contactType: ContactType, customAllowedContactCount: Int) =
    if (contactType == ContactType.CONTACT_TYPE_CUSTOM) {
        customAllowedContactsSubtitle(customAllowedContactCount)
    } else {
        stringResource(contactType.getNameResId())
    }