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

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fairphone.spring.launcher.data.model.Mode
import com.fairphone.spring.launcher.data.model.ModeState
import com.fairphone.spring.launcher.data.model.Presets
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme

@Composable
fun SwitchStateChangeHint(
    mode: Mode,
    modeState: ModeState,
    onAnimationDone: () -> Unit,
) {
    var startAnimation by remember { mutableStateOf(false) }
    val topLimitPadding = remember { 144.dp }
    val bottomLimitPadding = remember { 152.dp }
    val animationDurationMillis = remember { 1000 }

   // val transition = updateTransition(startAnimation, label = "switch state")

    val topPaddingAnim by animateDpAsState(
        label = "mode-switch-angle-anim",
        animationSpec = tween(
            durationMillis = animationDurationMillis,
            easing = LinearOutSlowInEasing
        ),
        targetValue = if (startAnimation) 16.dp else 0.dp,
        finishedListener = { onAnimationDone() }
    )

    val iconAngleAnim by animateFloatAsState(
        label = "mode-icon-angle-anim",
        animationSpec = tween(
            durationMillis = animationDurationMillis,
            easing = LinearOutSlowInEasing
        ),
        targetValue = if (startAnimation) 180f else 0f,
        finishedListener = {}
    )

    /*val topPaddingAnim by transition.animateDp(
        label = "mode-switch-angle-anim",
        transitionSpec = { tween(
            durationMillis = animationDurationMillis,
            easing = LinearOutSlowInEasing
        ) },
        targetValueByState = { state -> if (state) 180f else 0f },
    )
    val iconAngleAnim by transition.animateFloat(
        label = "mode-icon-angle-anim",
        transitionSpec = { tween(
            durationMillis = animationDurationMillis,
        ) },
        targetValueByState = { state -> if (state) 180f else 0f },
    )*/

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xF0000000),
                        Color.Transparent
                    )
                )
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(
                    end = 8.dp,
                    top = when (modeState) {
                        ModeState.ENABLED -> topLimitPadding + topPaddingAnim
                        ModeState.DISABLED -> bottomLimitPadding - topPaddingAnim
                    }

                )
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Top),
                horizontalAlignment = Alignment.End,
            ) {
                Icon(
                    imageVector = mode.icon,
                    contentDescription = "image description",
                    tint = Color.White,
                    modifier = Modifier.graphicsLayer(
                        rotationZ = iconAngleAnim,
                    )
                )
                Text(
                    text = "${mode.name}\n${modeState.name.lowercase()}",
                    style = TextStyle(
                        fontSize = 15.sp,
                        lineHeight = 18.sp,
                        //fontFamily = FontFamily(Font(R.font.inter)),
                        fontWeight = FontWeight.W600,
                        color = Color.White,
                        textAlign = TextAlign.Right,
                    )
                )
            }
            Box(
                //color = Color(0xFFD8FF4F),
                //shape = RoundedCornerShape(size = 1.dp),
                modifier = Modifier
                    .width(8.dp)
                    .height(92.dp)
                    .clip(RoundedCornerShape(size = 1.dp))
                    .background(color = Color(0xFFD8FF4F))
            )
        }
    }
    LaunchedEffect(Unit) {
        startAnimation = true
    }
}

@Composable
@Preview(showBackground = true)
fun SwitchStateChangeHint_Preview() {
    SpringLauncherTheme {
        SwitchStateChangeHint(
            mode = Presets.Spring,
            modeState = ModeState.ENABLED,
            onAnimationDone = {}
        )
    }
}