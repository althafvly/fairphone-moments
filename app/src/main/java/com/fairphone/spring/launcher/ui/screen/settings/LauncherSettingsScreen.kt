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

package com.fairphone.spring.launcher.ui.screen.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.fairphone.spring.launcher.ui.component.SettingsTopBar
import com.fairphone.spring.launcher.ui.navigation.SettingsNavigation
import com.fairphone.spring.launcher.ui.screen.settings.main.ProfileSettingsViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LauncherSettingsScreen(
    viewModel: ProfileSettingsViewModel = koinViewModel(),
    onCloseSettings: () -> Unit
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    val navController = rememberNavController()

    screenState ?: return

    Scaffold(
        topBar = {
            SettingsTopBar(
                navController = navController,
                profile = screenState!!.profile,
                onNavigateBack = {
                    if (!navController.navigateUp()) {
                        onCloseSettings()
                    }
                }
            )
        },
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                SettingsNavigation(navController = navController)
            }
        }
    )
}
