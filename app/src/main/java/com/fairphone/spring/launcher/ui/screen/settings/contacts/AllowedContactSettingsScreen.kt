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
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fairphone.spring.launcher.R
import com.fairphone.spring.launcher.data.model.ContactType
import com.fairphone.spring.launcher.data.model.Defaults
import com.fairphone.spring.launcher.ui.theme.Color_FP_Brand_Lime
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
                onContactTypeSelected = onContactTypeSelected
            )
        }
    }
}

@Composable
fun AllowedContactSettingsScreen(
    profileName: String,
    allowedContactTypeList: List<ContactType>,
    selectedContactType: ContactType,
    onContactTypeSelected: (ContactType) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
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
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            allowedContactTypeList.forEach { peopleType ->
                ContactTypeSelectorItem(
                    peopleType = peopleType,
                    isSelected = peopleType == selectedContactType,
                    onClick = { onContactTypeSelected(peopleType) }
                )
            }
        }
    }
}

@Composable
fun ContactTypeSelectorItem(
    peopleType: ContactType,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(size = 12.dp)
            )
            .clip(RoundedCornerShape(size = 12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        RadioButton(
            selected = isSelected,
            onClick = null,
            colors = RadioButtonDefaults.colors(
                selectedColor = Color_FP_Brand_Lime,
            )
        )

        Text(
            text = stringResource(peopleType.getNameResId()),
            style = FairphoneTypography.BodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
@Preview
fun AllowedContactSettingsScreen_Preview() {
    SpringLauncherTheme {
        AllowedContactSettingsScreen(
            profileName = "Essentials",
            allowedContactTypeList = Defaults.CONTACT_TYPE_LIST,
            selectedContactType = ContactType.CONTACT_TYPE_STARRED,
            onContactTypeSelected = {},
        )
    }
}

fun ContactType.getNameResId(): Int = when(this) {
    ContactType.CONTACT_TYPE_EVERYONE -> R.string.people_type_everyone
    ContactType.CONTACT_TYPE_NONE -> R.string.people_type_none
    ContactType.CONTACT_TYPE_ALL_CONTACTS -> R.string.people_type_all_contacts
    ContactType.CONTACT_TYPE_STARRED -> R.string.people_type_starred
    ContactType.CONTACT_TYPE_CUSTOM -> R.string.people_type_custom
    ContactType.UNRECOGNIZED -> R.string.people_type_none // fallback to 'none'
}