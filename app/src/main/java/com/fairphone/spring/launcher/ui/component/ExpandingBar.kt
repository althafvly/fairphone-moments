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

package com.fairphone.spring.launcher.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun ExpandingBar(height: Dp = 92.dp) {
    var enterAnimationGreenBar by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.height(92.dp)
    ) {
        AnimatedVisibility(
            visible = enterAnimationGreenBar,
            enter = expandVertically(
                animationSpec = tween(
                    durationMillis = ENTER_ANIMATION_DURATION,
                    easing = LinearOutSlowInEasing
                ),
                expandFrom = Alignment.Top,
                initialHeight = { 0 }
            ) + fadeIn(
                // Fade in with the initial alpha of 0.3f.
                initialAlpha = 0.3f
            ),
            exit = shrinkVertically(
                animationSpec = tween(
                    durationMillis = EXIT_ANIMATION_DURATION,
                    easing = LinearOutSlowInEasing
                ),
                shrinkTowards = Alignment.Bottom,
                targetHeight = { 0 }
            ),
        ) {
            Box(
                modifier = Modifier
                    .width(8.dp)
                    .height(height)
                    .clip(RoundedCornerShape(size = 1.dp))
                    .background(color = Color(0xFFD8FF4F))
            )
        }
    }

    LaunchedEffect(Unit) {
        enterAnimationGreenBar = true
        delay(1000)
        enterAnimationGreenBar = false
    }
}

@Composable
@Preview
fun ExpandingBar_Preview() {
    ExpandingBar()
}
