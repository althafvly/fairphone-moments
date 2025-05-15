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

package com.fairphone.spring.launcher.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.window.OnBackInvokedCallback
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.fairphone.spring.launcher.ui.navigation.HomeNavigation
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme
import com.fairphone.spring.launcher.util.Constants
import org.koin.compose.KoinContext

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

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        overrideActivityTransition(
            OVERRIDE_TRANSITION_OPEN,
            android.R.anim.fade_in,
            android.R.anim.fade_out
        )
        overrideActivityTransition(
            OVERRIDE_TRANSITION_CLOSE,
            android.R.anim.fade_in,
            android.R.anim.fade_out
        )
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT)
        )
        super.onCreate(savedInstanceState)
        instance = this

        setContent {
            KoinContext {
                SpringLauncherTheme {
                    HomeNavigation()
                }
            }
        }
    }

    private val onBackInInvokedCallback: OnBackInvokedCallback = object : OnBackInvokedCallback {
        override fun onBackInvoked() {
            // ignore back button
        }
    }

    @SuppressLint("WrongConstant")
    override fun onResume() {
        super.onResume()
        onBackInvokedDispatcher.registerOnBackInvokedCallback(0, onBackInInvokedCallback)
        hideGestureBar()
    }

    override fun onPause() {
        super.onPause()
        onBackInvokedDispatcher.unregisterOnBackInvokedCallback(onBackInInvokedCallback)
        showGestureBar()
    }

    private fun hideGestureBar() {
        window.insetsController?.apply {
            hide(android.view.WindowInsets.Type.navigationBars())
            systemBarsBehavior =
                android.view.WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    private fun showGestureBar() {
        window.insetsController?.apply {
            show(android.view.WindowInsets.Type.navigationBars())
            systemBarsBehavior =
                android.view.WindowInsetsController.BEHAVIOR_DEFAULT
        }
    }
}
