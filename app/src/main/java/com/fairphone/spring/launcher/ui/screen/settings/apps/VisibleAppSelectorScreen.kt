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

package com.fairphone.spring.launcher.ui.screen.settings.apps

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fairphone.spring.launcher.R
import com.fairphone.spring.launcher.data.model.AppInfo
import com.fairphone.spring.launcher.ui.component.ConfirmButton
import com.fairphone.spring.launcher.ui.component.SearchBar
import com.fairphone.spring.launcher.ui.component.SelectableListItem
import com.fairphone.spring.launcher.ui.component.SelectedListItem
import com.fairphone.spring.launcher.ui.theme.FairphoneTypography

@Composable
fun VisibleAppSelectorScreen(
    screenState: VisibleAppSelectorScreenState,
    filter: String,
    onFilterChanged: (String) -> Unit,
    onAppClick: (AppInfo) -> Unit = {},
    onAppDeselected: (AppInfo) -> Unit = {},
    onConfirmAppSelection: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (screenState) {
            is VisibleAppSelectorScreenState.Ready -> {
                VisibleAppSelectorScreen(
                    visibleApps = screenState.data.visibleApps,
                    appList = screenState.data.appList,
                    filter = filter,
                    showConfirmButton = screenState.data.showConfirmButton,
                    showAppCounter = screenState.data.showAppCounter,
                    showEmptyAppSelectedError = screenState.data.showEmptyAppSelectedError,
                    showMaxAppSelectedError = screenState.data.showMaxAppSelectedError,
                    onFilterChanged = onFilterChanged,
                    onAppClick = onAppClick,
                    onAppDeselected = onAppDeselected,
                    onConfirmAppSelection = onConfirmAppSelection,
                )
            }

            else -> {}
        }
    }
}

@Composable
fun VisibleAppSelectorScreen(
    appList: List<AppInfo>,
    visibleApps: List<AppInfo>,
    filter: String,
    showConfirmButton: Boolean,
    showAppCounter: Boolean,
    showEmptyAppSelectedError: Boolean,
    showMaxAppSelectedError: Boolean,
    onFilterChanged: (String) -> Unit,
    onAppClick: (AppInfo) -> Unit = {},
    onAppDeselected: (AppInfo) -> Unit = {},
    onConfirmAppSelection: () -> Unit,
) {
    Box(Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            SearchBar(
                query = filter,
                onQueryChange = onFilterChanged,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            SelectedAppsRow(
                selectedApps = visibleApps,
                onDeletedClick = onAppDeselected,
            )

            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(size = 12.dp)
                    )
                    .fillMaxSize()
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(size = 12.dp)
                    )
                    .clip(RoundedCornerShape(size = 12.dp))
            ) {
                items(
                    count = appList.size,
                    contentType = { index -> appList[index] },
                ) { index ->
                    val appInfo = appList[index]
                    val isSelected =
                        appInfo.packageName in visibleApps.map { it.packageName }
                    SelectableListItem(
                        icon = appInfo.icon,
                        title = appInfo.name,
                        isSelected = isSelected,
                        onClick = { onAppClick(appInfo) }
                    )
                }
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp),
        ) {
            when {
                showMaxAppSelectedError -> {
                    ErrorView(errorText = R.string.visible_apps_error_max_selected)
                }

                showEmptyAppSelectedError -> {
                    ErrorView(errorText = R.string.visible_apps_error_empty_selected)
                }

                showAppCounter -> {
                    MaximumAppCountReached()
                }
            }
            if (showConfirmButton) {
                ConfirmButton(
                    onClick = onConfirmAppSelection,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .padding(horizontal = 20.dp)
                )
            }
        }
    }
}

@Composable
fun MaximumAppCountReached(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(size = 100.dp)
            )
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(size = 100.dp)
            )
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = stringResource(R.string.visible_apps_count),
            style = FairphoneTypography.LabelMedium,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun ErrorView(
    modifier: Modifier = Modifier,
    @StringRes errorText: Int,
) {
    val bgGradientColors = remember {
        listOf(
            Color(0xFFFF4E4E),
            Color(0xFFFF1E1E),
        )
    }
    Box(
        modifier = modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(size = 100.dp)
            )
            .background(
                brush = Brush.horizontalGradient(bgGradientColors),
                shape = RoundedCornerShape(size = 100.dp)
            )
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = stringResource(errorText),
            style = FairphoneTypography.LabelMedium,
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun SelectedAppsRow(
    selectedApps: List<AppInfo>,
    onDeletedClick: (AppInfo) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(size = 12.dp)
            )
            .width(332.dp)
            .height(93.dp)
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(size = 12.dp)
            )
    ) {
        items(selectedApps.size) { index ->
            val appInfo = selectedApps[index]
            SelectedListItem(
                icon = appInfo.icon,
                title = appInfo.name,
                onDeleteClick = { onDeletedClick(appInfo) },
            )
        }
    }
}
