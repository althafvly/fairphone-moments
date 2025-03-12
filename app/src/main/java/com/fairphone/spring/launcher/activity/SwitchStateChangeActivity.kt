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

import android.app.Activity
import android.app.ActivityManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.View.OnAttachStateChangeListener
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.lifecycleScope
import com.fairphone.spring.launcher.data.model.State
import com.fairphone.spring.launcher.ui.theme.Color_Orange
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme
import kotlinx.coroutines.launch

const val EXTRA_SWITCH_BUTTON_STATE = "com.fairphone.spring.launcher.extra.switch_button_state"

class SwitchStateChangeActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT)
        )
        super.onCreate(savedInstanceState)

        // Enable blur behind
        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND)
            attributes.blurBehindRadius = 20 // Adjust blur radius

            // Make background transparent
            setBackgroundDrawableResource(android.R.color.transparent)
        }

        val switchButtonState = intent.getStringExtra(EXTRA_SWITCH_BUTTON_STATE)?.let { state ->
            State.valueOf(state)
        }

        setContent {
            SpringLauncherTheme {
                BlurBehindActivity {
                    Box(modifier = Modifier.fillMaxSize()) {}

                    LaunchedEffect(Unit) {
                        switchButtonState?.let {
                            onSwitchStateChangeAnimationDone(switchButtonState)
                        } ?: SpringLauncherHomeActivity.start(applicationContext)
                    }
                }
            }
        }
    }

    private fun onSwitchStateChangeAnimationDone(switchButtonState: State) = lifecycleScope.launch {
        when (switchButtonState) {
            State.DISABLED -> {
                startLauncherIntent()
            }

            State.ENABLED -> {
                //if (isStockLauncherRunning(applicationContext)) {
                SpringLauncherHomeActivity.start(applicationContext)
                //}
            }
        }

        //finish()
    }

    private fun isStockLauncherRunning(context: Context): Boolean {
        // getRunningTasks() is deprecated in API 29 and above, and may not return accurate results
        // Consider using UsageStatsManager for more reliable information
        // This is a fallback solution for cases where UsageStatsManager is not available or not granted
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningTasks = activityManager.getRunningTasks(10)
        return runningTasks.any { it.topActivity?.packageName == "com.android.launcher3" }
    }

    private fun setupWindowBlurListener() {
        val windowBlurEnabledListener: java.util.function.Consumer<Boolean> =
            object : java.util.function.Consumer<Boolean> {
                override fun accept(value: Boolean) {
                    updateWindowForBlurs(value)
                }
            }
        window.decorView.addOnAttachStateChangeListener(
            object : OnAttachStateChangeListener {
                @Override
                override fun onViewAttachedToWindow(v: View) {
                    windowManager.addCrossWindowBlurEnabledListener(
                        windowBlurEnabledListener
                    )
                }

                @Override
                override fun onViewDetachedFromWindow(v: View) {
                    windowManager.removeCrossWindowBlurEnabledListener(
                        windowBlurEnabledListener
                    )
                }
            });
    }

    private fun updateWindowForBlurs(blursEnabled: Boolean) {
        window.apply {
            setDimAmount(if (blursEnabled) .1f else .4f)
            setBackgroundBlurRadius(80)
            attributes.blurBehindRadius = 10
            attributes = attributes
        }
    }
}

fun Context.startLauncherIntent() {
    val intent = Intent(Intent.ACTION_MAIN).apply {
        addCategory(Intent.CATEGORY_HOME)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    startActivity(intent)
}

@Composable
fun BlurBehindActivity(content: @Composable () -> Unit) {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            FrameLayout(context).apply {
                // Enable blur behind (programmatically)
                val window = (context as Activity).window
                window.addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND)
                window.attributes.blurBehindRadius = 10
            }
        },
        update = { frameLayout ->
            // No need to set content here, as we want the blur behind the activity
        }
    )
    content() // Render the Compose content on top of the blurred background
}