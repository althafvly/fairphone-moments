/*
 * Copyright (c) 2025. Fairphone B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License
 *
 *         at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fairphone.spring.launcher.ui.screen.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.fairphone.spring.launcher.data.model.AppInfo
import com.fairphone.spring.launcher.data.repository.AppInfoRepository
import com.fairphone.spring.launcher.data.repository.IAppInfoRepository
import com.fairphone.spring.launcher.util.launchApp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class HomeScreenViewModel(
    app: Application,
    repository: IAppInfoRepository,
) : AndroidViewModel(app) {

    private val _screenState: MutableStateFlow<HomeScreenState> =
        MutableStateFlow(HomeScreenState())
    val screenState: StateFlow<HomeScreenState> = _screenState.asStateFlow()

    init {
        repository.getDefaultLauncherApps(context = getApplication()).let { homeApps ->
            _screenState.update { it.copy(appList = homeApps) }
        }
        viewModelScope.launch {
            while (isActive) {
                _screenState.update { it.copy(dateTime = LocalDateTime.now()) }
                delay(1000)
            }
        }
    }

    fun onAppClick(appInfo: AppInfo) {
        getApplication<Application>().launchApp(appInfo)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                return HomeScreenViewModel(
                    app = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]),
                    repository = AppInfoRepository(),
                ) as T
            }
        }
    }
}

data class HomeScreenState(
    val dateTime: LocalDateTime = LocalDateTime.now(),
    val appList: List<AppInfo> = emptyList(),
)
