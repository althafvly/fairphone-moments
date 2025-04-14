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

package com.fairphone.spring.launcher.ui.screen.settings.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fairphone.spring.launcher.data.model.AppInfo
import com.fairphone.spring.launcher.data.model.Moment
import com.fairphone.spring.launcher.data.model.Presets
import com.fairphone.spring.launcher.data.repository.IAppInfoRepository
import com.fairphone.spring.launcher.data.repository.IMomentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MomentSettingsViewModel(
    context: Context,
    private val appInfoRepository: IAppInfoRepository,
    private val momentRepository: IMomentRepository,
) : ViewModel() {

    private val _screenState = MutableStateFlow(MomentSettingsScreenState())
    val screenState = _screenState.asStateFlow()

    init {
        viewModelScope.launch {
            val currentMoment = momentRepository.getCurrentMoment()
            val visibleApps = appInfoRepository.getAppInfos(context, currentMoment.visibleApps)

            _screenState.update {
                it.copy(
                    moment = currentMoment,
                    visibleApps = visibleApps
                )
            }
        }
    }
}

data class MomentSettingsScreenState(
    val moment: Moment = Presets.Essentials,
    val visibleApps: List<AppInfo> = emptyList(),
)
