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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.fairphone.spring.launcher.R
import com.fairphone.spring.launcher.data.model.Presets
import com.fairphone.spring.launcher.data.model.getIconVector
import com.fairphone.spring.launcher.data.model.protos.LauncherProfile
import com.fairphone.spring.launcher.ui.navigation.AllowedContactSettings
import com.fairphone.spring.launcher.ui.navigation.VisibleAppSelector
import com.fairphone.spring.launcher.ui.navigation.VisibleAppSettings
import com.fairphone.spring.launcher.ui.theme.FairphoneTypography
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsTopBar(
    navController: NavHostController,
    onNavigateBack: () -> Unit,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val titleResId = currentDestination?.let {
        when {
            it.hasRoute<VisibleAppSettings>() -> R.string.setting_title_visible_apps
            it.hasRoute<VisibleAppSelector>() -> R.string.setting_title_visible_apps_selector
            it.hasRoute<AllowedContactSettings>() -> R.string.setting_title_allowed_contacts
            else -> null
        }
    }

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
        ),
        title = {
            if (titleResId != null) {
                Text(
                    text = stringResource(titleResId),
                    style = FairphoneTypography.H4,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
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
fun LauncherProfileSettingsTopBar(
    currentLauncherProfile: LauncherProfile,
    onEditLauncherProfileName: () -> Unit,
    onIconClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 40.dp)
    ) {
        // LauncherProfile Icon
        IconButton(
            modifier = Modifier
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
                ),
            onClick = onIconClick
        ) {
            Icon(
                imageVector = currentLauncherProfile.getIconVector(),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        // LauncherProfile name + edit button
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onEditLauncherProfileName() }
        ) {
            Text(
                text = currentLauncherProfile.name,
                style = FairphoneTypography.H4,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }

        // LauncherProfile active/inactive
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
//                text = stringResource(R.string.profile_state_active),
//                style = FairphoneTypography.BodySmall,
//                color = MaterialTheme.colorScheme.onSurface
//            )
//        }
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFEBEBE9)
fun LauncherProfileSettingsTopBar_Preview() {
    SpringLauncherTheme {
        LauncherProfileSettingsTopBar(
            currentLauncherProfile = Presets.Essentials.profile,
            onEditLauncherProfileName = {},
            onIconClick = {}
        )
    }
}
