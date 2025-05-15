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

package com.fairphone.spring.launcher.ui.screen.mode.creator

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fairphone.spring.launcher.R
import com.fairphone.spring.launcher.data.model.Presets
import com.fairphone.spring.launcher.ui.FP6Preview
import com.fairphone.spring.launcher.ui.FP6PreviewDark
import com.fairphone.spring.launcher.ui.component.PrimaryButton
import com.fairphone.spring.launcher.ui.component.ScreenHeader
import com.fairphone.spring.launcher.ui.screen.mode.ModeContainer
import com.fairphone.spring.launcher.ui.theme.FairphoneTypography
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme
import com.fairphone.spring.launcher.ui.theme.backgroundShapeBorderDarkStart
import com.fairphone.spring.launcher.ui.theme.backgroundShapeBorderLightStart

@Composable
fun ChooseBackgroundScreen(
    preset: Presets,
    onNavigateBack: () -> Unit,
    onNavigateClose: () -> Unit,
    onContinue: (Long, Long) -> Unit
) {
    val scrollState = rememberScrollState()
    val colors = Presets.entries.map { it.profile.bgColor2 }.distinct()
    var selectedColor by remember {
        mutableLongStateOf(colors.first { preset.profile.bgColor2 == it })
    }
    val width = LocalConfiguration.current.let {
        // the offset is depending on the screen width and the padding
        it.screenWidthDp * 2 - 20
    }

    LaunchedEffect(selectedColor) {
        scrollState.scrollTo(
            (colors.indexOf(selectedColor) * width).toInt()
        )
    }

    ModeContainer {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
        ) {
            CreateMomentTopBar(
                hasBackButton = true,
                onNavigateBack = onNavigateBack,
                onNavigateClose = onNavigateClose
            )

            ScreenHeader(
                stringResource(R.string.choose_app_background_header),
                modifier = Modifier.padding(horizontal = 40.dp),
                style = FairphoneTypography.H3
            )

            Row(
                Modifier
                    .horizontalScroll(scrollState)
                    .fillMaxWidth()
                    .padding(start = 76.dp, end = 76.dp, top = 40.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                colors.forEach { color ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(32.dp))
                            .border(
                                width = 12.dp,
                                color = if (isSystemInDarkTheme()) backgroundShapeBorderDarkStart else backgroundShapeBorderLightStart,
                                shape = RoundedCornerShape(32.dp)
                            )
                            .width(218.dp)
                            .height(446.dp)
                    ) {
                        ChooseBackgroundExample(
                            endColor = Color(color),
                            modifier = Modifier.fillMaxSize(),
                            onClick = {
                                selectedColor = color
                            }
                        )
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                colors.forEachIndexed { i, color ->
                    RadioButton(
                        selected = (color == selectedColor),
                        onClick = {
                            selectedColor = color
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1.0f))

            PrimaryButton(
                text = stringResource(R.string.bt_create),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                val preset = Presets.entries.first { it.profile.bgColor2 == selectedColor }
                onContinue(preset.profile.bgColor1, preset.profile.bgColor2)
            }
        }
    }
}


@Composable
private fun ChooseBackgroundScreen_Preview() {
    SpringLauncherTheme {
        ChooseBackgroundScreen(
            Presets.Balance,
            onNavigateBack = {},
            onNavigateClose = {},
            onContinue = { color1, color2 -> }
        )
    }
}

@Composable
@FP6Preview()
private fun ChooseBackgroundScreen_LightPreview() {
    ChooseBackgroundScreen_Preview()
}

@Composable
@FP6PreviewDark()
private fun ModeSwitcherScreen_DarkPreview() {
    ChooseBackgroundScreen_Preview()
}


