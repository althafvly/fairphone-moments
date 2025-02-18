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

package com.fairphone.spring.launcher.activity

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.fairphone.spring.launcher.data.model.State
import com.fairphone.spring.launcher.ui.component.SwitchStateChangeHintScreen
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val EXTRA_SWITCH_BUTTON_STATE = "com.fairphone.spring.launcher.extra.switch_button_state"

class SwitchStateChangeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT)
        )
        super.onCreate(savedInstanceState)

        val switchButtonState = intent.getStringExtra(EXTRA_SWITCH_BUTTON_STATE).let { state ->
            State.valueOf(state!!)
        }
        setContent {
            SpringLauncherTheme {
                SwitchStateChangeHintScreen(
                    modeTitle = "Deep focus",
                    state = switchButtonState,
                    onAnimationDone = { onSwitchStateChangeAnimationDone(switchButtonState) }
                )
            }
        }
    }

    private fun onSwitchStateChangeAnimationDone(switchButtonState: State) = lifecycleScope.launch {
        delay(500)
        when (switchButtonState) {
            State.DISABLED -> {
                startLauncherIntent()
            }
            State.ENABLED -> {
                if (isStockLauncherRunning(this@SwitchStateChangeActivity)) {
                    startLauncherIntent()
                }
            }
        }
        finish()
    }

    private fun isStockLauncherRunning(context: Context): Boolean {
        // getRunningTasks() is deprecated in API 29 and above, and may not return accurate results
        // Consider using UsageStatsManager for more reliable information
        // This is a fallback solution for cases where UsageStatsManager is not available or not granted
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningTasks = activityManager.getRunningTasks(10)
        return runningTasks.any { it.topActivity?.packageName == "com.android.launcher3" }
    }
}

fun Context.startLauncherIntent() {
    val intent = Intent(Intent.ACTION_MAIN).apply {
        addCategory(Intent.CATEGORY_HOME)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    startActivity(intent)
}