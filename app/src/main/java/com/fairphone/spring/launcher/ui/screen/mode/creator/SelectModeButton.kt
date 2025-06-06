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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fairphone.spring.launcher.R
import com.fairphone.spring.launcher.data.model.Preset
import com.fairphone.spring.launcher.ui.FP6Preview
import com.fairphone.spring.launcher.ui.FP6PreviewDark
import com.fairphone.spring.launcher.ui.icons.NavIcons
import com.fairphone.spring.launcher.ui.icons.mode.fromString
import com.fairphone.spring.launcher.ui.icons.nav.ChevronRight
import com.fairphone.spring.launcher.ui.theme.FairphoneTypography
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme
import com.fairphone.spring.launcher.ui.theme.actionButtonStrokeDark
import com.fairphone.spring.launcher.ui.theme.actionButtonStrokeLight
import com.fairphone.spring.launcher.ui.theme.modeButtonBackgroundDark
import com.fairphone.spring.launcher.ui.theme.modeButtonBackgroundLight
import com.fairphone.spring.launcher.ui.theme.pressedActionButtonEndGradienDark
import com.fairphone.spring.launcher.ui.theme.pressedActionButtonEndGradienLight
import com.fairphone.spring.launcher.ui.theme.pressedActionButtonStartGradienDark
import com.fairphone.spring.launcher.ui.theme.pressedActionButtonStartGradienLight
import com.fairphone.spring.launcher.ui.theme.selectedActionButtonStrokeEndGradientDark
import com.fairphone.spring.launcher.ui.theme.selectedActionButtonStrokeEndGradientLight

@Composable
fun SelectModeButton(
    presetProfile: Preset,
    modifier: Modifier = Modifier,
    onModeSettingsClick: (Preset) -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isDark = isSystemInDarkTheme()
    val isPressed by interactionSource.collectIsPressedAsState()

    Button(
        onClick = { onModeSettingsClick(presetProfile) },
        border = computeButtonBorder(isDark = isDark),
        interactionSource = interactionSource,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = computeButtonBackground(
                isDark = isDark,
                isPressed = isPressed
            ),
            contentColor = MaterialTheme.colorScheme.onBackground,
        ),
        contentPadding = PaddingValues(0.dp),
        modifier = modifier
            .fillMaxWidth()
            .computeButtonModifier(
                isPressed = isPressed,
                isDark = isDark
            ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start),
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
        ) {
            Icon(
                imageVector = ImageVector.fromString(presetProfile.icon),
                contentDescription = presetProfile.name,
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .size(20.dp)
            )

            Column(
                modifier = Modifier.weight(2f)
            ) {
                Text(
                    text = stringResource(presetProfile.title),
                    style = FairphoneTypography.BodyMedium,
                )
                Text(
                    text = stringResource(presetProfile.subtitle),
                    style = FairphoneTypography.BodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(
                onClick = { onModeSettingsClick(presetProfile) },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = NavIcons.ChevronRight,
                    contentDescription = stringResource(R.string.add_mode_screen_header),
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

private fun Modifier.computeButtonModifier(isPressed: Boolean, isDark: Boolean): Modifier =
    if (isPressed)
        this
            .background(
                Brush
                    .radialGradient(
                        if (isDark)
                            listOf(
                                pressedActionButtonStartGradienDark,
                                pressedActionButtonEndGradienDark,
                            ) else listOf(
                            pressedActionButtonStartGradienLight,
                            pressedActionButtonEndGradienLight,
                        ),
                        radius = 300.0f,
                    ),
                shape = RoundedCornerShape(16.dp),
            )
    else this

private fun computeButtonBackground(isDark: Boolean, isPressed: Boolean): Color {
    if (isPressed) {
        return Color.Transparent
    }
    return if (isDark) modeButtonBackgroundDark else modeButtonBackgroundLight
}

private fun computeButtonBorder(isDark: Boolean): BorderStroke {
    return BorderStroke(
        width = 1.dp,
        brush = Brush.horizontalGradient(
            listOf(
                if (isDark) actionButtonStrokeDark else actionButtonStrokeLight,
                if (isDark) selectedActionButtonStrokeEndGradientDark else selectedActionButtonStrokeEndGradientLight
            )
        )
    )
}

@Composable
fun SelectModeButton_Preview() {
    SpringLauncherTheme {
        Column {
            SelectModeButton(Preset.QualityTime)
            SelectModeButton(Preset.DeepFocus)
        }

    }
}

@Composable
@FP6Preview()
fun SelectModeButton_LightPreview() {
    SelectModeButton_Preview()
}

@Composable
@FP6PreviewDark()
fun SelectModeButton_DarkPreview() {
    SelectModeButton_Preview()
}