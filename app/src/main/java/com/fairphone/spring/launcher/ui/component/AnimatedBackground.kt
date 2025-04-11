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

package com.fairphone.spring.launcher.ui.component

import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.ExperimentalAnimationSpecApi
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fairphone.spring.launcher.data.model.Presets
import com.fairphone.spring.launcher.data.model.colors
import com.fairphone.spring.launcher.ui.FP6Preview
import kotlinx.coroutines.delay

/**
 * FP6 specs:
 *
 * width: 1116 px
 * height: 2484 px
 *
 * pixel per dp = 3
 *
 * width: 372.0.dp dp
 * height: 828.0.dp dp
 *
 * General Information
 * Frame Rate: 120 fps
 * Conversion: 1 second = 120 frames
 * Standard transition duration: 0.133s (16 frames)
 * For elements with gradual position changes, timing varies as per keyframes.
 */

enum class AnimationState {
    START, END
}

@OptIn(ExperimentalAnimationSpecApi::class)
@Composable
fun AnimatedBackground(
    modifier: Modifier = Modifier,
    colors: Pair<Color, Color>,
    content: @Composable BoxScope.() -> Unit = {},
) {
    var animationState by remember { mutableStateOf(AnimationState.START) }
    val transition = updateTransition(targetState = animationState, label = "bg anim")
    LaunchedEffect(transition) {
        delay(100)
        animationState = AnimationState.END
    }

    val correctionX = 186.dp
    val correctionY = 500.dp

    Box(modifier = modifier.fillMaxSize()) {
        val offsetAnimationSpec = remember {
            tween<Dp>(
                durationMillis = 800,
                easing = EaseOutCubic,
            )
        }
        val rotationAnimationSpec = remember {
            tween<Float>(
                delayMillis = 400,
                durationMillis = 400,
                easing = EaseOutCubic,
            )
        }

        val leftBlobRotation by transition.animateFloat(
            targetValueByState = { state ->
                when (state) {
                    AnimationState.START -> 0f
                    AnimationState.END -> -35f
                }
            },
            transitionSpec = {
                rotationAnimationSpec

//                keyframesWithSpline {
//                    delayMillis = 400
//                    durationMillis = 400
//                    leftBLobStartRotation at 0
//                    // TODO: Add intermediate steps?
//                    leftBLobEndRotation atFraction 1f
//                }
            }
        )
        val rightBlobRotation by transition.animateFloat(
            targetValueByState = { state ->
                when (state) {
                    AnimationState.START -> 0f
                    AnimationState.END -> 35f
                }
            },
            transitionSpec = {
                rotationAnimationSpec

//                keyframesWithSpline {
//                    delayMillis = 400
//                    durationMillis = 400
//                    rightBLobStartRotation at 0
//                    // TODO: Add intermediate steps?
//                    rightBLobEndRotation atFraction 1f
//                }
            }
        )

        val leftBlobXDp by transition.animateDp(
            targetValueByState = { state ->
                when (state) {
                    AnimationState.START -> 186.dp - correctionX
                    AnimationState.END -> 6.dp - correctionX
                }
            },
            transitionSpec = {
                offsetAnimationSpec
            }
        )
        val leftBlobYDp by transition.animateDp(
            targetValueByState = { state ->
                when (state) {
                    AnimationState.START -> 1206.dp - correctionY
                    AnimationState.END -> 826.dp - correctionY
                }
            },
            transitionSpec = {
                offsetAnimationSpec
            }
        )
        val rightBlobXDp by transition.animateDp(
            targetValueByState = { state ->
                when (state) {
                    AnimationState.START -> 186.dp - correctionX
                    AnimationState.END -> 366.dp - correctionX
                }
            },
            transitionSpec = {
                offsetAnimationSpec
            }
        )
        val rightBlobYDp by transition.animateDp(
            targetValueByState = { state ->
                when (state) {
                    AnimationState.START -> 1208.dp - correctionY
                    AnimationState.END -> 828.dp - correctionY
                }
            },
            transitionSpec = {
                offsetAnimationSpec
            }
        )

        BackgroundBlob(
            color = colors.first,
            modifier = Modifier
                .offset(x = leftBlobXDp, y = leftBlobYDp)
                .rotate(leftBlobRotation)
        )

        BackgroundBlob(
            color = colors.second,
            modifier = Modifier
                .offset(x = rightBlobXDp, y = rightBlobYDp)
                .rotate(rightBlobRotation)
        )

        content()
    }
}

@Composable
fun BackgroundBlob(
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = Blob(),
        color = color,
        modifier = modifier
            .blur(radius = 100.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
            .width(392.dp)
            .height(676.dp)
    ) {}
}

@Composable
@FP6Preview
fun AnimatedBackground_Preview() {
    AnimatedBackground(
        colors = Presets.Essentials.colors(),
    )
}
