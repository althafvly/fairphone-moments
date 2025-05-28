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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fairphone.spring.launcher.ui.navigation.CreateModeNavigation
import com.fairphone.spring.launcher.ui.navigation.SelectMode

@Composable
fun CreateModeScreen(
    onCloseModeCreator: () -> Unit,
    onModeCreated: () -> Unit,
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        topBar = {
            CreateMomentTopBar(
                hasBackButton = currentDestination?.hasRoute<SelectMode>() == false,
                onNavigateBack = { navController.navigateUp() },
                onNavigateClose = { onCloseModeCreator() }
            )
        },
        content = {
            Box(modifier = Modifier.padding(it)) {
                CreateModeNavigation(
                    navController = navController,
                    onModeCreated = onModeCreated,
                )
            }
        }
    )
}
