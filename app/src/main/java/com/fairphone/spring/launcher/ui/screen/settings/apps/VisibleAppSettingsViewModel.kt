/*
 * Copyright (C) 2025 FairPhone B.V.
 *
 * SPDX-FileCopyrightText: 2025. FairPhone B.V.
 *
 * SPDX-License-Identifier: EUPL-1.2
 */

package com.fairphone.spring.launcher.ui.screen.settings.apps

/*
 * Copyright (c) 2025. FairPhone B.V.
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

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fairphone.spring.launcher.data.model.AppInfo
import com.fairphone.spring.launcher.data.model.protos.LauncherProfileApp
import com.fairphone.spring.launcher.data.repository.AppInfoRepository
import com.fairphone.spring.launcher.domain.usecase.profile.GetEditedProfileUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class VisibleAppSettingsViewModel(
    context: Context,
    private val appInfoRepository: AppInfoRepository,
    private val getEditedProfileUseCase: GetEditedProfileUseCase,
) : ViewModel() {

    val screenState: StateFlow<VisibleAppSettingsScreenState> =
        getEditedProfileUseCase.execute(Unit)
            .map { profile ->
                val visibleApps = getAppInfoList(context, profile.launcherProfileAppsList)
                VisibleAppSettingsScreenState.Ready(
                    visibleApps = visibleApps,
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = VisibleAppSettingsScreenState.Loading
            )


    private fun getAppInfoList(context: Context, appIds: List<LauncherProfileApp>): List<AppInfo> {
        return appInfoRepository.getAppInfosByProfileApps(context, appIds)
    }
}

sealed interface VisibleAppSettingsScreenState {
    object Loading : VisibleAppSettingsScreenState
    data class Ready(val visibleApps: List<AppInfo>) :
        VisibleAppSettingsScreenState
}
