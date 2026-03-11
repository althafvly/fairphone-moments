/*
 * Copyright (C) 2025 FairPhone B.V.
 *
 * SPDX-FileCopyrightText: 2025. FairPhone B.V.
 *
 * SPDX-License-Identifier: EUPL-1.2
 */

package com.fairphone.spring.launcher.activity

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import com.fairphone.spring.launcher.ui.screen.LauncherHomeScreen
import com.fairphone.spring.launcher.ui.screen.home.PermissionsScreen
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme
import com.fairphone.spring.launcher.util.Constants
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val ON_FINISH_DELAY = 400L

class SpringLauncherHomeActivity : ComponentActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, SpringLauncherHomeActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or
                        Intent.FLAG_ACTIVITY_NO_ANIMATION
            }
            context.startActivity(intent)
        }

        private var instance: SpringLauncherHomeActivity? = null

        fun stop() {
            Log.d(Constants.LOG_TAG, "Stopping SpringLauncherHomeActivity")
            instance?.finish()
        }
    }

    private val isContentVisibleState = mutableStateOf(false)

    private val permissionRefreshTrigger = mutableIntStateOf(0)

    fun hasAllRequiredPermissions(context: Context): Boolean {
        val notificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        return notificationManager.isNotificationPolicyAccessGranted &&
                Settings.System.canWrite(context) &&
                Settings.canDrawOverlays(context)
    }

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instance = this

        setContent {
            var hasPermissions by rememberSaveable { mutableStateOf(false) }

            LaunchedEffect(permissionRefreshTrigger.intValue) {
                hasPermissions = hasAllRequiredPermissions(this@SpringLauncherHomeActivity)
            }

            if (!hasPermissions) {
                SpringLauncherTheme {
                    PermissionsScreen(context = this@SpringLauncherHomeActivity)
                }
            } else {
                LauncherHomeScreen(
                    isContentVisibleState = isContentVisibleState,
                )
            }
        }
    }

    override fun finish() {
        finishWithDelay()
    }

    private fun finishWithDelay() {
        isContentVisibleState.value = false
        lifecycleScope.launch {
            delay(ON_FINISH_DELAY) // delay set to let the exit animation show properly
            super.finish()
        }
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            // ignore back button
        }
    }

    @SuppressLint("WrongConstant")
    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            onBackInvokedDispatcher.registerOnBackInvokedCallback(0) {
                // ignore back button
            }
        } else {
            onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        }
        hideGestureBar()
        permissionRefreshTrigger.value++
    }

    override fun onPause() {
        super.onPause()
        onBackPressedCallback.remove()
        showGestureBar()
    }

    private fun hideGestureBar() {
        WindowCompat.getInsetsController(window, window.decorView).apply {
            hide(WindowInsetsCompat.Type.navigationBars())
            systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    private fun showGestureBar() {
        WindowCompat.getInsetsController(window, window.decorView).apply {
            show(WindowInsetsCompat.Type.navigationBars())
            systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
        }
    }
}
