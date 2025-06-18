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

import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.fairphone.spring.launcher.data.model.ContactInfo
import com.fairphone.spring.launcher.ui.component.selector.ItemSelectorLayout

@Composable
fun AllowedContactSelectorScreen(
    screenState: AllowedContactSelectorScreenState,
    onContactPermissionChanged: (Boolean) -> Unit,
    onContactSelected: (ContactInfo) -> Unit,
    onContactDeselected: (ContactInfo) -> Unit,
    onConfirmContactSelection: () -> Unit,
) {
    val contactPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {
        onContactPermissionChanged(it)
    }

    LaunchedEffect(screenState) {
        if (screenState is AllowedContactSelectorScreenState.RequestContactPermission) {
            Log.d("AllowedContactSelector", "Requesting contact permission")
            contactPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (screenState) {
            is AllowedContactSelectorScreenState.Success -> {
                ItemSelectorLayout(
                    itemList = screenState.screenData.allContacts,
                    selectedItems = screenState.screenData.selectedContacts,
                    showConfirmButton = screenState.screenData.showConfirmButton,
                    onItemClick = onContactSelected,
                    onItemDeselected = onContactDeselected,
                    onConfirmItemSelection = onConfirmContactSelection,
                    maxItemCount = Int.MAX_VALUE,
                    showItemCounter = false,
                    showEmptyItemSelectedError = false,
                    showMaxItemSelectedError = false,
                    maxItemCountErrorText = null,
                    emptyItemSelectedErrorText = null,
                )
            }

            is AllowedContactSelectorScreenState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            else -> {}
        }
    }
}
