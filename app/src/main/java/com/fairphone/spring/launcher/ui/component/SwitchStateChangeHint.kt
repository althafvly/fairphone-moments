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
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fairphone.spring.launcher.data.model.Presets
import com.fairphone.spring.launcher.data.model.State
import com.fairphone.spring.launcher.ui.theme.FairphoneTypography
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme
import kotlinx.coroutines.delay

const val ENTER_ANIMATION_DURATION = 500
const val ANIMATION_STILL_DURATION = 1000
const val EXIT_ANIMATION_DURATION = 500

@Composable
fun SwitchStateChangeHintScreen(
    modeTitle: String,
    state: State,
    onAnimationDone: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Black.copy(alpha = 0.6f),
                        Color.Transparent
                    )
                )
            )
    ) {
        SwitchStateChangeHint(
            modeTitle = modeTitle,
            state = state,
            onAnimationDone = onAnimationDone,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(
                    end = 8.dp,
                    top = when (state) {
                        State.ENABLED -> 158.dp
                        State.DISABLED -> 152.dp
                    },
                )
        )
    }
}

fun State.getGreenBarHintAlignment(): Alignment.Vertical {
    return when (this) {
        State.ENABLED -> Alignment.Top
        State.DISABLED -> Alignment.Bottom
    }
}

@Composable
fun SwitchStateChangeHint(
    modeTitle: String,
    state: State,
    onAnimationDone: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var greenBarAlignment by remember { mutableStateOf(state.getGreenBarHintAlignment()) }
    val visibilityState = remember { MutableTransitionState(false) }
    val transition = rememberTransition(visibilityState)
    val iconRotationAngle by transition.animateFloat(
        targetValueByState = { visible -> if (visible) 180f else 0f },
        transitionSpec = {
            tween(
                durationMillis = ENTER_ANIMATION_DURATION,
                easing = LinearOutSlowInEasing,
                delayMillis = ENTER_ANIMATION_DURATION
            )
        }
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
        modifier = modifier.defaultMinSize(minHeight = 92.dp)
    ) {
        AnimatedVisibility(
            visibleState = visibilityState,
            enter = fadeIn(
                animationSpec = tween(
                    durationMillis = ENTER_ANIMATION_DURATION,
                    easing = LinearOutSlowInEasing
                )
            ),
            exit = fadeOut(
                animationSpec = tween(
                    durationMillis = EXIT_ANIMATION_DURATION,
                    easing = LinearOutSlowInEasing
                )
            ),
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Top),
                horizontalAlignment = Alignment.End,
            ) {
                Icon(
                    imageVector = Presets.Spring.icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.rotate(iconRotationAngle)
                )
                Text(
                    text = "$modeTitle\n${state.name.lowercase()}",
                    style = FairphoneTypography.SwitchLabel,
                    color = Color.White,
                    textAlign = TextAlign.Right,
                )
            }
        }

        AnimatedVisibility(
            visibleState = visibilityState,
            enter = expandVertically(
                expandFrom = Alignment.Top,
                animationSpec = tween(
                    durationMillis = ENTER_ANIMATION_DURATION,
                    easing = LinearOutSlowInEasing
                ),
            ) + fadeIn(
                initialAlpha = 0.3f
            ),
            exit = shrinkVertically(
                shrinkTowards = Alignment.Bottom,
                animationSpec = tween(
                    durationMillis = EXIT_ANIMATION_DURATION,
                    easing = LinearOutSlowInEasing
                )
            ),
            modifier = Modifier
                .align(alignment = greenBarAlignment)
                .width(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(8.dp)
                    .height(92.dp)
                    .clip(RoundedCornerShape(size = 1.dp))
                    .background(color = Color(0xFFD8FF4F))

            )
        }
    }
    LaunchedEffect(Unit) {
        delay(ENTER_ANIMATION_DURATION.toLong())
        visibilityState.targetState = true
        delay(ANIMATION_STILL_DURATION.toLong())
        greenBarAlignment = if (greenBarAlignment == Alignment.Top) Alignment.Bottom else Alignment.Top
        visibilityState.targetState = false
        delay(EXIT_ANIMATION_DURATION.toLong().times(2))
        onAnimationDone()
    }
}

@Composable
@Preview
fun SwitchStateChangeHintEnabled_Preview() {
    SpringLauncherTheme {
        SwitchStateChangeHint(
            modeTitle = "Deep focus",
            state = State.ENABLED,
            onAnimationDone = {}
        )
    }
}

@Composable
@Preview()
fun SwitchStateChangeHintDisabled_Preview() {
    SpringLauncherTheme {
        SwitchStateChangeHint(
            modeTitle = "Deep focus",
            state = State.DISABLED,
            onAnimationDone = {}
        )
    }
}
