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
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import com.fairphone.spring.launcher.ui.component.AnimatedBackground
import com.fairphone.spring.launcher.ui.component.Color_Shape_1
import com.fairphone.spring.launcher.ui.component.Color_Shape_2
import com.fairphone.spring.launcher.ui.screen.home.HomeScreen
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme
import kotlinx.coroutines.delay

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

        setContent {
            SpringLauncherTheme {
                val density = LocalDensity.current
                Box(modifier = Modifier.fillMaxSize().onGloballyPositioned { coordinates ->
                    Log.d("onGloballyPositioned", coordinates.toString())
                    Log.d("onGloballyPositioned", "width: ${coordinates.size.width} px")
                    Log.d("onGloballyPositioned", "height: ${coordinates.size.height} px")

                    with (density) {
                        Log.d("onGloballyPositioned", "width: ${coordinates.size.width.toDp()} dp")
                        Log.d("onGloballyPositioned", "height: ${coordinates.size.height.toDp()} dp")
                    }
                })
                var visibility by remember { mutableStateOf(false) }
                AnimatedVisibility(
                    visible = visibility,
                    enter = expandVertically(
                        expandFrom = Alignment.Top,
                        animationSpec = tween(
                            durationMillis = 200
                        )
                    ),
                    exit = shrinkVertically(
                        shrinkTowards = Alignment.Top,
                        animationSpec = tween(
                            durationMillis = 200
                        )
                    ),
                ) {
                    AnimatedBackground(
                        colors = Pair(Color_Shape_1, Color_Shape_2),
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                //.background(MaterialTheme.colorScheme.background.copy(alpha = 0.7f))
                        ) {
                            HomeScreen()
                        }
                    }
                }

                LaunchedEffect(Unit) {
                    delay(100)
                    visibility = true
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
    }

    override fun onPause() {
        super.onPause()
        onBackInvokedDispatcher.unregisterOnBackInvokedCallback(onBackInInvokedCallback)
    }
}
