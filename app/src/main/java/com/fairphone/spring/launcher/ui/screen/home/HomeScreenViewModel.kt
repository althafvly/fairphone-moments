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

package com.fairphone.spring.launcher.ui.screen.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fairphone.spring.launcher.data.model.AppInfo
import com.fairphone.spring.launcher.data.model.Default
import com.fairphone.spring.launcher.data.model.Moment
import com.fairphone.spring.launcher.data.repository.IAppInfoRepository
import com.fairphone.spring.launcher.data.repository.IMomentRepository
import com.fairphone.spring.launcher.util.launchApp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class HomeScreenViewModel(
    private val appInfoRepository: IAppInfoRepository,
    private val momentRepository: IMomentRepository
) : ViewModel() {

    private val _screenState: MutableStateFlow<HomeScreenState> =
        MutableStateFlow(HomeScreenState())
    val screenState: StateFlow<HomeScreenState> = _screenState.asStateFlow()

    fun refreshUiState(context: Context) {
        viewModelScope.launch {
            val currentMoment = momentRepository.getActiveMoment().first()
            appInfoRepository.getAppInfos(context, currentMoment.visibleAppsList).let { homeApps ->
                _screenState.update { it.copy(appList = homeApps) }
            }

            while (isActive) {
                _screenState.update { it.copy(dateTime = LocalDateTime.now()) }
                delay(1000)
            }
        }
    }

    fun onAppClick(context: Context, appInfo: AppInfo) {
        context.launchApp(appInfo)
    }
}

data class HomeScreenState(
    val dateTime: LocalDateTime = LocalDateTime.now(),
    val appList: List<AppInfo> = emptyList(),
    val currentMoment: Moment = Default.DefaultMoment,
)
