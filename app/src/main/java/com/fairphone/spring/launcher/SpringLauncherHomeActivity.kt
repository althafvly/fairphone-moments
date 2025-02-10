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

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.onGloballyPositioned
import com.fairphone.spring.launcher.data.model.Mode
import com.fairphone.spring.launcher.data.model.ModeState
import com.fairphone.spring.launcher.data.model.Presets
import com.fairphone.spring.launcher.ui.component.SwitchStateChangeHint
import com.fairphone.spring.launcher.ui.screen.home.HomeScreen
import com.fairphone.spring.launcher.ui.screen.mode.ModeSwitcherScreen
import com.fairphone.spring.launcher.ui.theme.Color_FP_Brand_Sand
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme

class SpringLauncherHomeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            SpringLauncherTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    var showHint by remember { mutableStateOf(true) }
                    if (showHint) {
                        SwitchStateChangeHint(
                            mode = Presets.Spring,
                            modeState = ModeState.ENABLED,
                            onAnimationDone = { showHint = false }
                        )
                    }
                    SpringLauncherScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }


}

@Composable
fun SpringLauncherScreen(modifier: Modifier = Modifier) {
    var isSwitchingMode by remember { mutableStateOf(false) }
    var currentMode: Mode by remember { mutableStateOf(Presets.Spring) }
    var height by remember { mutableFloatStateOf(0f) }
    var width by remember { mutableFloatStateOf(0f) }
    val offsetX = remember { Animatable(0f) }

    LaunchedEffect(currentMode) {
        offsetX.snapTo(0f)
        offsetX.animateTo(
            targetValue = width,
            animationSpec = tween(
                durationMillis = 2000,
                delayMillis = 100,
                easing = FastOutSlowInEasing
            ),
        )
    }

//    val offset by animateFloatAsState(
//        targetValue = width,
//        animationSpec = tween(
//            durationMillis = 3000,
//            delayMillis = 200,
//            easing = FastOutSlowInEasing
//        ),
//    )

    val gradientColors = currentMode.bgColors.toMutableList().apply {
        add(0, Color_FP_Brand_Sand)
        add(0, Color_FP_Brand_Sand)
    }

    val background = Brush.linearGradient(
        colors = gradientColors,
        start = Offset(offsetX.value, 0f),
        end = Offset(width - offsetX.value, Float.POSITIVE_INFINITY),
        tileMode = TileMode.Clamp,
    )
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(background)
            .onGloballyPositioned {
                height = it.size.height.toFloat()
                width = it.size.width.toFloat()
            }
    ) {


        if (isSwitchingMode) {
            ModeSwitcherScreen(
                currentMode = currentMode,
                onModeSelected = {
                    currentMode = it
                    isSwitchingMode = false
                }
            )
        } else {
            HomeScreen()
        }
    }
}
