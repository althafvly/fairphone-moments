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

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fairphone.spring.launcher.data.model.LauncherProfile
import com.fairphone.spring.launcher.ui.screen.mode.ModeSwitcherScreen
import com.fairphone.spring.launcher.ui.screen.mode.ModeSwitcherViewModel
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme
import org.koin.android.ext.android.inject
import org.koin.compose.KoinContext

class ModeSwitcherActivity : ComponentActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, ModeSwitcherActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or
                        Intent.FLAG_ACTIVITY_NO_ANIMATION
            }
            context.startActivity(intent)
        }
    }

    private val profileSwitcherViewModel: ModeSwitcherViewModel by inject<ModeSwitcherViewModel>()

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
        val selectProfile: (LauncherProfile) -> Unit = {
            // TODO send the selected profile
            LauncherSettingsActivity.start(this)
        }

        super.onCreate(savedInstanceState)
        setContent {
            KoinContext {
                SpringLauncherTheme {
                    val screenState by profileSwitcherViewModel.screenState.collectAsStateWithLifecycle()
                    if (screenState != null) {
                        ModeSwitcherScreen(
                            currentLauncherProfile = screenState!!.activeProfile,
                            profiles = screenState!!.profiles,
                            onModeSelected = selectProfile,
                            onCancel = { finish() }
                        )
                    }
                }
            }
        }
    }
}

