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

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.fairphone.spring.launcher.ui.PreviewDark
import com.fairphone.spring.launcher.ui.PreviewLight
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme
import com.fairphone.spring.launcher.ui.theme.switchBackgroundDarkColor
import com.fairphone.spring.launcher.ui.theme.switchBackgroundLightColor
import com.fairphone.spring.launcher.ui.theme.switchBorderEndDarkColor
import com.fairphone.spring.launcher.ui.theme.switchBorderEndLightColor
import com.fairphone.spring.launcher.ui.theme.switchBorderStartDarkColor
import com.fairphone.spring.launcher.ui.theme.switchBorderStartLightColor
import com.fairphone.spring.launcher.ui.theme.switchCheckedBackground
import com.fairphone.spring.launcher.ui.theme.switchThumbColor

/**
 * To apply the expected style we had to implement our own switch button
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwitchButton(
    state: Boolean,
    modifier: Modifier = Modifier,
    onToggle: (Boolean) -> Unit = {}
) {

    val isDark = isSystemInDarkTheme()
    var checked by remember { mutableStateOf(state) }

    val margin = 3.dp
    val width = 56.dp
    val height = 24.dp
    val thumbWidth = 28.dp
    val thumbHeigth = 18.dp
    val radius = 56.dp

    // To move thumb, we need to calculate the position (along x axis)
    val animatePosition = animateFloatAsState(
        targetValue = with(LocalDensity.current) {
            if (checked) (width - thumbWidth - margin).toPx() else margin.toPx()
        }
    )

    Canvas(
        modifier = modifier
            .clip(RoundedCornerShape(radius))
            .size(width = width, height = height)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        checked = !checked
                        onToggle(checked)
                    }
                )
            }
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    listOf(
                        if (isDark) switchBorderStartDarkColor else switchBorderStartLightColor,
                        if (isDark) switchBorderEndDarkColor else switchBorderEndLightColor
                    )
                ),
                shape = RoundedCornerShape(radius)
            )
            .background(
                if (checked) {
                    switchCheckedBackground
                } else if (isDark) {
                    switchBackgroundDarkColor
                } else {
                    switchBackgroundLightColor
                }
            )
    ) {
        // Thumb
        drawRoundRect(
            size = Size(width = thumbWidth.toPx(), height = thumbHeigth.toPx()),
            color = switchThumbColor,
            cornerRadius = CornerRadius(x = radius.toPx(), y = radius.toPx()),
            topLeft = Offset(
                x = animatePosition.value,
                y = margin.toPx()
            )
        )
    }
}

@Composable
private fun SwitchButton_Preview() {
    SpringLauncherTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SwitchButton(state = true)
            SwitchButton(state = false)
        }
    }
}

@PreviewLight
@Composable
private fun SwitchButton_LightPreview() {
    SwitchButton_Preview()
}

@PreviewDark
@Composable
private fun SwitchButton_DarkPreview() {
    SwitchButton_Preview()
}