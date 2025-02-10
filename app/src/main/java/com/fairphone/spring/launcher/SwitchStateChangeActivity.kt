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

package com.fairphone.spring.launcher

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.fairphone.spring.launcher.data.model.ModeState
import com.fairphone.spring.launcher.data.model.Presets
import com.fairphone.spring.launcher.ui.component.SwitchStateChangeHint
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme

class SwitchStateChangeActivity : ComponentActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, SwitchStateChangeActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        window.setWindowAnimations(R.style.FadeInAnimation)
        // Make the activity transparent
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        setContent {
            SpringLauncherTheme {
                var showHint by remember { mutableStateOf(true) }
                if (showHint) {
                    SwitchStateChangeHint(
                        mode = Presets.Spring,
                        modeState = ModeState.ENABLED,
                        onAnimationDone = {
                            showHint = false
                            finish()
                        }
                    )
                }
            }
        }
    }
}
