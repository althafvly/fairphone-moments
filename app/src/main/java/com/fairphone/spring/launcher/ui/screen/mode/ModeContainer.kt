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

package com.fairphone.spring.launcher.ui.screen.mode

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.fairphone.spring.launcher.ui.FP6Preview
import com.fairphone.spring.launcher.ui.FP6PreviewDark
import com.fairphone.spring.launcher.ui.theme.Color_FP_Brand_Lime
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme

@Composable
fun ModeContainer(
    endColor: Color? = null,
    onClick: () -> Unit = {},
    screenContent: @Composable BoxScope.() -> Unit
) {
    val modifier: Modifier = if(endColor != null) {
        val colors = arrayOf(
            0.0f to MaterialTheme.colorScheme.background,
            0.3f to MaterialTheme.colorScheme.background,
            1f to endColor
        )
        Modifier
            .background(
                brush = Brush.linearGradient(
                    colorStops = colors,
                    start = Offset(600.0f, 400.0f),
                    end = Offset(-1000.0f, Float.POSITIVE_INFINITY),
                )
            )
    } else {
        Modifier.background(MaterialTheme.colorScheme.background)
    }

    Box(modifier = modifier.clickable { onClick() }) {
        screenContent()
    }
}

@Composable
private fun ModeContainer_Preview() {
    SpringLauncherTheme {
        ModeContainer(Color_FP_Brand_Lime) {

        }
    }
}

@Composable
@FP6Preview()
private fun ModeContainer_LightPreview() {
    ModeContainer_Preview()
}

@Composable
@FP6PreviewDark()
private fun ModeContainer_DarkPreview() {
    ModeContainer_Preview()
}
