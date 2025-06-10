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

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import com.fairphone.spring.launcher.data.model.Presets
import com.fairphone.spring.launcher.ui.FP6Preview
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme
import kotlinx.coroutines.delay

@Composable
fun CardWithAnimatedBorder(
    modifier: Modifier = Modifier,
    enableAnimation: Boolean = true,
    backgroundColor: Color = Color.White,
    borderColors: List<Color> = emptyList(),
    onCardClick: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    if (enableAnimation) {
        val animationDuration = 2000L
        var playing by remember { mutableStateOf(true) }
        val infiniteTransition = rememberInfiniteTransition()
        val angle by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec =
                infiniteRepeatable(
                    animation = tween(animationDuration.toInt(), easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                )
        )

        val brush =
            if (borderColors.isNotEmpty()) Brush.sweepGradient(borderColors)
            else Brush.sweepGradient(listOf(Color.Gray, Color.White))

        LaunchedEffect (true) {
            delay(animationDuration)
            playing = false
        }
        Surface(
            modifier = modifier.clickable { onCardClick() },
            shape = RoundedCornerShape(12.dp)
        ) {
            Surface(
                modifier =
                    Modifier
                        .clipToBounds()
                        .padding(2.dp)
                        .drawWithContent {
                            if(playing) {
                                rotate(angle) {
                                    drawCircle(
                                        brush = brush,
                                        radius = size.width,
                                        blendMode = BlendMode.SrcIn,
                                    )
                                }
                            } else {
                                drawCircle(
                                    brush = brush,
                                    radius = size.width,
                                    blendMode = BlendMode.SrcIn,
                                )
                            }
                            drawContent()
                        },
                color = backgroundColor,
                shape = RoundedCornerShape(12.dp)
            ) {
                content()
            }
        }
    } else {
        Box(modifier = modifier.clip(RoundedCornerShape(12.dp))) {
            content()
        }
    }
}

@FP6Preview
@Composable
fun CardWithAnimatedBorder_Preview() {
    SpringLauncherTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(8.dp)
        ) {
            CardWithAnimatedBorder(
                borderColors = listOf(
                    Color(Presets.QualityTime.profile.bgColor1),
                    Color(Presets.QualityTime.profile.bgColor2),
                ),
                enableAnimation = false
            ) {
                Text(
                    text = "Non animated border",
                    modifier = Modifier.padding(8.dp)
                )
            }

            CardWithAnimatedBorder(
                borderColors = listOf(
                    Color(Presets.QualityTime.profile.bgColor1),
                    Color(Presets.QualityTime.profile.bgColor2),
                ),
                enableAnimation = true
            ) {
                Text(
                    text = "Animated Border",
                    modifier = Modifier.padding(8.dp)
                )
            }
        }

    }
}