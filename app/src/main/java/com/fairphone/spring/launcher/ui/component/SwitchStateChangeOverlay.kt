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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fairphone.spring.launcher.R
import com.fairphone.spring.launcher.data.model.LauncherProfile
import com.fairphone.spring.launcher.data.model.Presets
import com.fairphone.spring.launcher.data.model.SwitchState
import com.fairphone.spring.launcher.data.model.getIconVector
import com.fairphone.spring.launcher.ui.theme.FairphoneTypography
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme
import kotlinx.coroutines.delay

const val ENTER_ANIMATION_DURATION = 500
const val ANIMATION_STILL_DURATION = 1000
const val EXIT_ANIMATION_DURATION = 500

@Composable
fun SwitchStateChangeOverlayScreen(
    profile: LauncherProfile,
    switchState: SwitchState,
    onAnimationDone: () -> Unit,
    visibilityState: MutableTransitionState<Boolean>,
) {
    val transition = rememberTransition(visibilityState)
    val background by transition.animateColor(
        targetValueByState = { visible -> if (visible) Color.Black.copy(alpha = 0.6f) else Color.Transparent },
        transitionSpec = {
            tween()
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        background,
                        Color.Transparent
                    )
                )
            )
    ) {
        SwitchStateChangeOverlay(
            profile = profile,
            switchState = switchState,
            onAnimationDone = onAnimationDone,
            visibilityState = visibilityState,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(
                    end = 8.dp,
                    top = when (switchState) {
                        SwitchState.ENABLED -> 158.dp
                        SwitchState.DISABLED -> 152.dp
                    },
                )
        )
    }
}

fun SwitchState.getGreenBarHintAlignment(): Alignment.Vertical {
    return when (this) {
        SwitchState.ENABLED -> Alignment.Top
        SwitchState.DISABLED -> Alignment.Bottom
    }
}

@Composable
fun SwitchStateChangeOverlay(
    profile: LauncherProfile,
    switchState: SwitchState,
    onAnimationDone: () -> Unit,
    modifier: Modifier = Modifier,
    visibilityState: MutableTransitionState<Boolean>,
) {
    var greenBarAlignment by remember { mutableStateOf(switchState.getGreenBarHintAlignment()) }
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
    val overlayText = when (switchState) {
        SwitchState.ENABLED ->
            stringResource(R.string.profile_switch_state_enabled, profile.name)

        SwitchState.DISABLED ->
            stringResource(R.string.profile_switch_state_disabled, profile.name)
    }

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
                    imageVector = profile.getIconVector(),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.rotate(iconRotationAngle)
                )
                Text(
                    text = overlayText,
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
        greenBarAlignment =
            if (greenBarAlignment == Alignment.Top) Alignment.Bottom else Alignment.Top
        visibilityState.targetState = false
        delay(EXIT_ANIMATION_DURATION.toLong().times(2))
        onAnimationDone()
    }
}

@Composable
@Preview
fun SwitchStateChangeHintEnabled_Preview() {
    SpringLauncherTheme {
        SwitchStateChangeOverlay(
            profile = Presets.Essentials.profile,
            switchState = SwitchState.ENABLED,
            onAnimationDone = {},
            visibilityState = remember { MutableTransitionState(false) },
        )
    }
}

@Composable
@Preview()
fun SwitchStateChangeHintDisabled_Preview() {
    SpringLauncherTheme {
        SwitchStateChangeOverlay(
            profile = Presets.Essentials.profile,
            switchState = SwitchState.DISABLED,
            onAnimationDone = {},
            visibilityState = remember { MutableTransitionState(false) },
        )
    }
}
