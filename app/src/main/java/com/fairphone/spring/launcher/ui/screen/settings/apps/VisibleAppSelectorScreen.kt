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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fairphone.spring.launcher.R
import com.fairphone.spring.launcher.data.model.AppInfo
import com.fairphone.spring.launcher.ui.FP6Preview
import com.fairphone.spring.launcher.ui.component.ConfirmButton
import com.fairphone.spring.launcher.ui.component.SelectableListItem
import com.fairphone.spring.launcher.ui.component.SelectedListItem
import com.fairphone.spring.launcher.ui.theme.FairphoneTypography
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun VisibleAppSelectorScreen(
    onConfirmAppSelection: () -> Unit = {},
    viewModel: VisibleAppSettingsViewModel = koinViewModel()
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    VisibleAppSelectorScreen(
        screenState = screenState,
        onAppClick = viewModel::onAppClick,
        onAppDeselected = viewModel::removeVisibleApp,
        onConfirmAppSelection = {
            viewModel.confirmAppSelection()
            onConfirmAppSelection()
        }
    )
}

@Composable
fun VisibleAppSelectorScreen(
    modifier: Modifier = Modifier,
    screenState: VisibleAppSelectorScreenState,
    onAppClick: (AppInfo) -> Unit = {},
    onAppDeselected: (AppInfo) -> Unit = {},
    onConfirmAppSelection: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
//            AppSearchBar(
//                query = "",
//                onQueryChange = {},
//                onSearch = {},
//                searchResults = listOf("App 1", "App 2", "App 3"),
//                onResultClick = {},
//                modifier = Modifier.padding(horizontal = 20.dp)
//            )

            SelectedAppsRow(
                selectedApps = screenState.visibleApps,
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
                    count = screenState.appList.size,
                    contentType = { index -> screenState.appList[index] },
                ) { index ->
                    val appInfo = screenState.appList[index]
                    val isSelected =
                        appInfo.packageName in screenState.visibleApps.map { it.packageName }
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
                screenState.showMaxAppSelectedError -> {
                    ErrorView(errorText = R.string.visible_apps_error_max_selected)
                }

                screenState.showEmptyAppSelectedError -> {
                    ErrorView(errorText = R.string.visible_apps_error_empty_selected)
                }

                screenState.showAppCounter -> {
                    MaximumAppCountReached()
                }
            }
            if (screenState.showConfirmButton) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    searchResults: List<String>,
    onResultClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    supportingContent: (@Composable (String) -> Unit)? = null,
    leadingContent: (@Composable () -> Unit)? = null,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier
            .fillMaxWidth()
            .semantics { isTraversalGroup = true }
    ) {
        SearchBar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .semantics { traversalIndex = 0f },
            inputField = {
                SearchBarDefaults.InputField(
                    query = query,
                    onQueryChange = onQueryChange,
                    onSearch = {
                        onSearch(query)
                        expanded = false
                    },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = { Text("Search apps") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },

                    )
            },
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {
            /*// Show search results in a lazy column for better performance
            LazyColumn {
                items(count = searchResults.size) { index ->
                    val resultText = searchResults[index]
                    ListItem(
                        headlineContent = { Text(resultText) },
                        supportingContent = supportingContent?.let { { it(resultText) } },
                        leadingContent = leadingContent,
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                        modifier = Modifier
                            .clickable {
                                onResultClick(resultText)
                                expanded = false
                            }
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }
            }*/
        }
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


@Composable
@FP6Preview
fun VisibleAppSelectorScreen_Preview() {
    SpringLauncherTheme {
        VisibleAppSelectorScreen(

        )
    }
}