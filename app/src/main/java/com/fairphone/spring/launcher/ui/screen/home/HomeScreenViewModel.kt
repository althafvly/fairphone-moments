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
import com.fairphone.spring.launcher.data.model.LauncherProfile
import com.fairphone.spring.launcher.data.prefs.AppPrefs
import com.fairphone.spring.launcher.data.prefs.UsageMode
import com.fairphone.spring.launcher.data.repository.AppInfoRepository
import com.fairphone.spring.launcher.domain.usecase.profile.GetActiveProfileUseCase
import com.fairphone.spring.launcher.domain.usecase.profile.InitializeSpringLauncherUseCase
import com.fairphone.spring.launcher.domain.usecase.profile.SetApplicationUsageModeUseCase
import com.fairphone.spring.launcher.util.isDeviceInRetailDemoMode
import com.fairphone.spring.launcher.util.launchApp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class HomeScreenViewModel(
    context: Context,
    getActiveProfileUseCase: GetActiveProfileUseCase,
    private val setApplicationUsageModeUseCase: SetApplicationUsageModeUseCase,
    private val appPrefs: AppPrefs,
    private val appInfoRepository: AppInfoRepository,
    private val initializeSpringLauncherUseCase: InitializeSpringLauncherUseCase,
) : ViewModel() {

    private val _dateTime: MutableStateFlow<LocalDateTime> = MutableStateFlow(LocalDateTime.now())
    val dateTime: StateFlow<LocalDateTime> = _dateTime.asStateFlow()

    val screenState: StateFlow<HomeScreenState?> =
        getActiveProfileUseCase.execute(Unit).map { profile ->
            val visibleApps =
                appInfoRepository.getAppInfosByPackageNames(context, profile.visibleAppsList)
            val isRetailDemoMode = context.isDeviceInRetailDemoMode()
            HomeScreenState(
                activeProfile = profile,
                visibleApps = visibleApps,
                isRetailDemoMode = isRetailDemoMode,
                appUsageMode = appPrefs.usageMode()
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = null,
            )

    init {
        refreshTime()
        initializeSpringLauncher()
    }

    private fun refreshTime() = viewModelScope.launch {
        while (isActive) {
            _dateTime.update { LocalDateTime.now() }
            delay(1000)
        }
    }

    fun onAppClick(context: Context, appInfo: AppInfo) {
        viewModelScope.launch {
            context.launchApp(appInfo)
        }
    }

    fun initializeSpringLauncher() {
        viewModelScope.launch {
            initializeSpringLauncherUseCase.execute(Unit)
        }
    }

    /**
     * When user has clicked on ‘X’ on the onboarding tooltip, or interact with another component
     * on the screen (i.e., the Mode button or an app) the onboarding is complete and the usage
     * mode is the default one
     */
    fun finishOnBoarding() {
        viewModelScope.launch {
            if (screenState.value?.appUsageMode == UsageMode.ON_BOARDING_COMPLETE) {
                setApplicationUsageModeUseCase.execute(UsageMode.DEFAULT)
            }
        }
    }
}

data class HomeScreenState(
    val activeProfile: LauncherProfile,
    val visibleApps: List<AppInfo> = emptyList(),
    val isRetailDemoMode: Boolean = false,
    val appUsageMode: UsageMode = UsageMode.ON_BOARDING
)
