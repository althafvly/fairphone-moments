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

package com.fairphone.spring.launcher.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.fairphone.spring.launcher.data.model.Default
import com.fairphone.spring.launcher.data.model.Moment
import com.fairphone.spring.launcher.data.model.getIconVector
import com.fairphone.spring.launcher.ui.navigation.VisibleAppSelector
import com.fairphone.spring.launcher.ui.navigation.VisibleAppSettings
import com.fairphone.spring.launcher.ui.theme.FairphoneTypography
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsTopBar(
    navController: NavHostController,
    moment: Moment,
    onNavigateBack: () -> Unit,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val title = currentDestination?.let {
        when {
            //it.hasRoute<MomentSettings>() -> "Moment settings"
            it.hasRoute<VisibleAppSettings>() -> "Visible apps"
            it.hasRoute<VisibleAppSelector>() -> "Select visible apps"
            else -> ""
        }
    } ?: ""

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
        ),
        title = {
            Text(
                text = title,
                style = FairphoneTypography.H4,
                color = MaterialTheme.colorScheme.onBackground,
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Localized description"
                )
            }
        },
    )
}

@Composable
fun MomentSettingsTopBar(
    currentMoment: Moment,
    onEditMomentName: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 40.dp)
    ) {
        // Moment Icon
        Box(
            Modifier
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(size = 12.dp)
                )
                .width(64.dp)
                .height(64.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(size = 12.dp)
                )
        ) {
            Icon(
                imageVector = currentMoment.getIconVector(),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Moment name + edit button
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onEditMomentName() }
        ) {
            Text(
                text = currentMoment.name,
                style = FairphoneTypography.H4,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }

        // Moment active/inactive
//        Box(
//            Modifier
//                .border(
//                    width = 1.dp,
//                    color = MaterialTheme.colorScheme.outline,
//                    shape = RoundedCornerShape(size = 100.dp)
//                )
//                .width(74.dp)
//                .height(36.dp)
//                .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 8.dp)
//        ) {
//
//            Text(
//                text = stringResource(R.string.moment_state_active),
//                style = FairphoneTypography.BodySmall,
//                color = MaterialTheme.colorScheme.onSurface
//            )
//        }
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFEBEBE9)
fun MomentSettingsTopBar_Preview() {
    SpringLauncherTheme {
        MomentSettingsTopBar(
            currentMoment = Default.DefaultMoment,
            onEditMomentName = {}
        )
    }
}
