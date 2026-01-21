/*
 * Copyright (C) 2025 FairPhone B.V.
 *
 * SPDX-FileCopyrightText: 2025. FairPhone B.V.
 *
 * SPDX-License-Identifier: EUPL-1.2
 */

package com.fairphone.spring.launcher.activity.viewmodel

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fairphone.spring.launcher.data.model.SwitchState
import com.fairphone.spring.launcher.data.model.protos.LauncherProfile
import com.fairphone.spring.launcher.domain.usecase.EnableDndUseCase
import com.fairphone.spring.launcher.domain.usecase.profile.GetActiveProfileUseCase
import com.fairphone.spring.launcher.domain.usecase.profile.InitializeSpringLauncherUseCase
import com.fairphone.spring.launcher.receiver.CallInterceptorReceiver
import com.fairphone.spring.launcher.service.NotificationInterceptorService
import com.fairphone.spring.launcher.util.isDoNotDisturbAccessGranted
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class SwitchStateChangeViewModel(
    private val getActiveProfileUseCase: GetActiveProfileUseCase,
    private val enableDndUseCase: EnableDndUseCase,
    private val initializeSpringLauncherUseCase: InitializeSpringLauncherUseCase,
) : ViewModel() {

    val activeProfile: StateFlow<LauncherProfile?> =
        getActiveProfileUseCase.execute(Unit)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    init {
        initializeSpringLauncher()
    }

    fun initializeSpringLauncher() {
        viewModelScope.launch {
            initializeSpringLauncherUseCase.execute(Unit)
        }
    }

    fun handleDnd(context: Context, switchState: SwitchState) = viewModelScope.launch {
        val enableDnd = when (switchState) {
            SwitchState.ENABLED -> true
            SwitchState.DISABLED -> false
        }
        enableDndUseCase.execute(enableDnd)
        // Don't disable components for now since it is changing the Notification Access Permission State
        // handleNotificationInterceptorStates(context, enableDnd)
    }

    private fun handleNotificationInterceptorStates(context: Context, enable: Boolean) {
        val receiverState = if (enable) {
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED
        } else {
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED
        }

        // Enable or disable the BroadcastReceiver and the Service
        context.packageManager.setComponentEnabledSetting(
            ComponentName(context, CallInterceptorReceiver::class.java),
            receiverState,
            PackageManager.DONT_KILL_APP
        )
        context.packageManager.setComponentEnabledSetting(
            ComponentName(context, NotificationInterceptorService::class.java),
            receiverState,
            PackageManager.DONT_KILL_APP
        )
    }

    fun handleLockscreenWallpaper(context: Context, switchState: SwitchState) {
        val enableDnd = when (switchState) {
            SwitchState.ENABLED -> true
            SwitchState.DISABLED -> false
        }
        // TODO: Enable lockscreen wallpaper switch when fixed
        // LockscreenWallpaperSwitcherWorker.enqueueWallpaperWork(context, enableDnd)
    }

    fun isDndPermissionGranted(context: Context): Boolean {
        return context.isDoNotDisturbAccessGranted()
    }
}
