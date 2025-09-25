/*
 * Copyright (C) 2025 FairPhone B.V.
 *
 * SPDX-FileCopyrightText: 2025. FairPhone B.V.
 *
 * SPDX-License-Identifier: EUPL-1.2
 */

package com.fairphone.spring.launcher.ui.navigation

import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.fairphone.spring.launcher.data.model.protos.ContactType
import com.fairphone.spring.launcher.ui.screen.settings.contacts.AllowedContactSelectorScreen
import com.fairphone.spring.launcher.ui.screen.settings.contacts.AllowedContactSelectorViewModel
import com.fairphone.spring.launcher.ui.screen.settings.contacts.AllowedContactSettingsScreen
import com.fairphone.spring.launcher.ui.screen.settings.contacts.AllowedContactSettingsViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
object AllowedContactSettings

@Serializable
object AllowedCustomContactSelector

const val ACTION_LIST_STARRED = "com.android.contacts.action.LIST_STARRED"

fun NavGraphBuilder.allowedContactSettingsNavGraph(navController: NavHostController) {
    //Allowed Contact Settings Screen
    composable<AllowedContactSettings> {
        val viewModel: AllowedContactSettingsViewModel = koinViewModel()
        val screenState by viewModel.screenState.collectAsStateWithLifecycle()

        AllowedContactSettingsScreen(
            screenState = screenState,
            onContactTypeSelected = { contactType ->
                viewModel.onContactTypeSelected(contactType)
                if (contactType == ContactType.CONTACT_TYPE_CUSTOM) {
                    navController.navigate(AllowedCustomContactSelector)
                }
            },
            onOpenStarredContacts = {
                val intent = Intent(ACTION_LIST_STARRED)
                navController.context.startActivity(intent)
            }
        )
    }

    composable<AllowedCustomContactSelector> {
        val viewModel: AllowedContactSelectorViewModel = koinViewModel()
        val screenState by viewModel.screenState.collectAsStateWithLifecycle()

        AllowedContactSelectorScreen(
            screenState = screenState,
            onContactPermissionChanged = { isGranted ->
                viewModel.onContactPermissionChanged(isGranted)
                if (!isGranted) {
                    navController.popBackStack()
                }
            },
            onContactSelected = { contactInfo ->
                viewModel.onContactSelected(contactInfo)
            },
            onContactDeselected = { contactInfo ->
                viewModel.onContactDeselected(contactInfo)
            },
            onConfirmContactSelection = {
                viewModel.confirmContactSelection()
                navController.popBackStack()
            }
        )
    }
}
