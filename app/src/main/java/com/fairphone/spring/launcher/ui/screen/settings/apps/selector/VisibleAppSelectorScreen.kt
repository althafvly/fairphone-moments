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

package com.fairphone.spring.launcher.ui.screen.settings.apps.selector

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.fairphone.spring.launcher.R
import com.fairphone.spring.launcher.data.model.AppInfo
import com.fairphone.spring.launcher.data.model.Presets
import com.fairphone.spring.launcher.ui.FP6Preview
import com.fairphone.spring.launcher.ui.FP6PreviewDark
import com.fairphone.spring.launcher.ui.component.selector.ItemSelectorLayout
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme
import com.fairphone.spring.launcher.util.fakeApp

@Composable
fun VisibleAppSelectorScreen(
    screenState: VisibleAppSelectorScreenState,
    onAppClick: (AppInfo) -> Unit,
    onAppDeselected: (AppInfo) -> Unit,
    onConfirmAppSelection: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (screenState) {
            is VisibleAppSelectorScreenState.Ready -> {
                ItemSelectorLayout(
                    itemList = screenState.data.appList,
                    selectedItems = screenState.data.visibleApps,
                    maxItemCount = screenState.data.maxItemCount,
                    showConfirmButton = screenState.data.showConfirmButton,
                    showItemCounter = screenState.data.showAppCounter,
                    showEmptyItemSelectedError = screenState.data.showEmptyAppSelectedError,
                    showMaxItemSelectedError = screenState.data.showMaxAppSelectedError,
                    confirmButtonTextResource = screenState.data.confirmButtonTextResource,
                    onItemClick = onAppClick,
                    onItemDeselected = onAppDeselected,
                    onConfirmItemSelection = onConfirmAppSelection,
                    maxItemCountErrorText = stringResource(
                        R.string.visible_apps_error_max_selected,
                        screenState.data.maxItemCount,
                    ),
                    emptyItemSelectedErrorText = stringResource(
                        R.string.visible_apps_error_empty_selected,
                    ),
                )
            }

            else -> {}
        }
    }
}

@Composable
private fun VisibleAppSelectorScreen_Preview() {
    val context = LocalContext.current
    SpringLauncherTheme {
        VisibleAppSelectorScreen(
            screenState = VisibleAppSelectorScreenState.Ready(
                data = ScreenData(
                    profileId = Presets.Essentials.profile.id,
                    appList = listOf(
                        context.fakeApp("test"),
                        context.fakeApp("test1"),
                        context.fakeApp("test2")
                    ),
                    visibleApps = listOf(context.fakeApp("test")),
                    showConfirmButton = true,
                    showAppCounter = false,
                    showEmptyAppSelectedError = false,
                    showMaxAppSelectedError = false,
                    confirmButtonTextResource = R.string.bt_confirm
                )
            ),
            onAppClick = {},
            onAppDeselected = {},
            onConfirmAppSelection = {},
        )
    }
}

@Composable
@FP6Preview()
private fun VisibleAppSelectorScreen_LightPreview() {
    VisibleAppSelectorScreen_Preview()
}

@Composable
@FP6PreviewDark()
private fun VisibleAppSelectorScreen_DarkPreview() {
    VisibleAppSelectorScreen_Preview()
}