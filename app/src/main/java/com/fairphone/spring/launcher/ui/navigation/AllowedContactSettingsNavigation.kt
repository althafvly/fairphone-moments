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

package com.fairphone.spring.launcher.ui.navigation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.fairphone.spring.launcher.ui.screen.settings.contacts.AllowedContactSettingsScreen
import com.fairphone.spring.launcher.ui.screen.settings.contacts.AllowedContactSettingsViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
object AllowedContactSettings

fun NavGraphBuilder.allowedContactSettingsNavGraph(navController: NavHostController) {
    //Allowed Contact Settings Screen
    composable<AllowedContactSettings> {
        val viewModel: AllowedContactSettingsViewModel = koinViewModel()
        val screenState by viewModel.screenState.collectAsStateWithLifecycle()

        AllowedContactSettingsScreen(
            screenState = screenState,
            onContactTypeSelected = { contactType ->
                viewModel.onContactTypeSelected(contactType)
            },
            onAllowRepeatCallsStateChanged = {
                viewModel.updateRepeatCallEnabledIndicator(it)
            },
        )
    }
}
