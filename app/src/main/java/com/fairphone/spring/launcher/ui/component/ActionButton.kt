/*
 * Copyright (C) 2025 FairPhone B.V.
 *
 * SPDX-FileCopyrightText: 2025. FairPhone B.V.
 *
 * SPDX-License-Identifier: EUPL-1.2
 */

package com.fairphone.spring.launcher.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fairphone.spring.launcher.data.model.LauncherColors
import com.fairphone.spring.launcher.ui.FP6Preview
import com.fairphone.spring.launcher.ui.FP6PreviewDark
import com.fairphone.spring.launcher.ui.icons.NavIcons
import com.fairphone.spring.launcher.ui.icons.mode.SettingsIcon
import com.fairphone.spring.launcher.ui.icons.nav.ArrowLeft
import com.fairphone.spring.launcher.ui.icons.nav.Close
import com.fairphone.spring.launcher.ui.theme.Color_FP_Brand_Lime
import com.fairphone.spring.launcher.ui.theme.FairphoneTypography
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme
import com.fairphone.spring.launcher.ui.theme.actionButtonBackgroundDark
import com.fairphone.spring.launcher.ui.theme.actionButtonBackgroundLight
import com.fairphone.spring.launcher.ui.theme.actionButtonStrokeDark
import com.fairphone.spring.launcher.ui.theme.actionButtonStrokeLight
import com.fairphone.spring.launcher.ui.theme.focusActionButtonStrokeDark
import com.fairphone.spring.launcher.ui.theme.focusActionButtonStrokeLight
import com.fairphone.spring.launcher.ui.theme.onBackgroundLight
import com.fairphone.spring.launcher.ui.theme.pressedActionButtonEndGradienDark
import com.fairphone.spring.launcher.ui.theme.pressedActionButtonEndGradienLight
import com.fairphone.spring.launcher.ui.theme.pressedActionButtonStartGradienDark
import com.fairphone.spring.launcher.ui.theme.pressedActionButtonStartGradienLight
import com.fairphone.spring.launcher.ui.theme.selectedActionButtonBackgroundDark
import com.fairphone.spring.launcher.ui.theme.selectedActionButtonBackgroundLight
import com.fairphone.spring.launcher.ui.theme.selectedActionButtonStrokeEndGradientDark
import com.fairphone.spring.launcher.ui.theme.selectedActionButtonStrokeEndGradientLight

enum class ButtonSize { Small, Default, Big }
enum class ButtonType {
    Round, RoundedCorner;

    fun shape(): RoundedCornerShape =
        when (this) {
            Round -> RoundedCornerShape(50.dp)
            RoundedCorner -> RoundedCornerShape(12.dp)
        }
}

@Composable
fun ActionButton(
    icon: ImageVector,
    labelText: String? = null,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    size: ButtonSize = ButtonSize.Default,
    type: ButtonType = ButtonType.Round,
    selectedStartColor: Color = Color_FP_Brand_Lime,
    selectedEndColor: Color = Color.White,
    onClick: () -> Unit = {}
) {
    Column(
        horizontalAlignment = horizontalAlignment,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val isDark = isSystemInDarkTheme()
        val isPressed by interactionSource.collectIsPressedAsState()
        val isFocus by interactionSource.collectIsFocusedAsState()
        val isHover by interactionSource.collectIsHoveredAsState()

        val (buttonSize, iconSize, paddingValue) = when (size) {
            ButtonSize.Default -> listOf(48.dp, 24.dp, 12.dp)
            ButtonSize.Small -> listOf(32.dp, 16.dp, 4.dp)
            ButtonSize.Big -> listOf(64.dp, 36.dp, 18.dp)
        }

        Button(
            shape = type.shape(),
            onClick = { onClick() },
            colors = ButtonDefaults.textButtonColors(
                containerColor = computeButtonBackground(
                    isDark = isDark,
                    isSelected = isSelected,
                    isPressed = isPressed
                ),
                contentColor = MaterialTheme.colorScheme.onBackground
            ),
            interactionSource = interactionSource,
            border = if (size == ButtonSize.Default) {
                computeButtonBorder(
                    isDark = isDark,
                    isFocus = isFocus || isHover,
                    isSelected = isSelected,
                    selectedStartColor = selectedStartColor,
                    selectedEndColor = selectedEndColor
                )
            } else {
                null
            },
            contentPadding = PaddingValues(paddingValue),
            modifier = Modifier
                .computeButtonModifier(isPressed = isPressed, isDark = isDark)
                .width(buttonSize)
                .height(buttonSize)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = labelText,
                tint = if (isSelected) onBackgroundLight else MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .width(iconSize)
                    .height(iconSize)
            )
        }
        if (labelText != null) {
            Text(
                text = labelText,
                style = FairphoneTypography.ButtonLegend,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
            )
        }
    }
}

private fun Modifier.computeButtonModifier(
    isPressed: Boolean,
    isDark: Boolean
): Modifier =
    if (isPressed) this
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
                    )
                ), shape = RoundedCornerShape(50.dp)
        )
    else this

fun computeButtonBackground(
    isDark: Boolean,
    isSelected: Boolean,
    isPressed: Boolean
): Color {
    if (isSelected) {
        return if (isDark) selectedActionButtonBackgroundDark else selectedActionButtonBackgroundLight
    }
    if (isPressed) {
        return Color.Transparent
    }
    return if (isDark) actionButtonBackgroundDark else actionButtonBackgroundLight
}


private fun computeButtonBorder(
    isDark: Boolean,
    isFocus: Boolean,
    isSelected: Boolean,
    selectedStartColor: Color,
    selectedEndColor: Color
): BorderStroke {
    if (isFocus) {
        return BorderStroke(
            width = 1.dp,
            color = if (isDark) focusActionButtonStrokeDark else focusActionButtonStrokeLight
        )
    }

    val stroke = if (isDark) {
        if (isSelected) selectedStartColor else actionButtonStrokeDark
    } else {
        if (isSelected) selectedStartColor else actionButtonStrokeLight
    }
    return BorderStroke(
        width = 1.dp,
        brush = Brush.verticalGradient(
            listOf(
                stroke,
                if (isSelected) selectedEndColor else if (isDark) selectedActionButtonStrokeEndGradientDark else selectedActionButtonStrokeEndGradientLight
            )
        )
    )
}

@Composable
fun ActionButton_Preview() {
    SpringLauncherTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ActionButton(
                icon = Icons.Filled.Add,
                labelText = "Create Moment",
            )
            ActionButton(
                icon = SettingsIcon,
            )
            ActionButton(
                icon = SettingsIcon,
                labelText = "Update selected moment",
                isSelected = true,
                selectedStartColor = Color(LauncherColors.QualityTime.rightColor),
                selectedEndColor = Color(LauncherColors.QualityTime.leftColor)
            )
            ActionButton(
                icon = SettingsIcon,
                labelText = "Update selected moment",
                isSelected = true,
                selectedStartColor = Color(LauncherColors.Custom.rightColor),
                selectedEndColor = Color(LauncherColors.Custom.leftColor)
            )
            ActionButton(
                icon = NavIcons.Close,
                size = ButtonSize.Small
            )
            ActionButton(
                icon = NavIcons.ArrowLeft,
                size = ButtonSize.Small
            )
            ActionButton(
                icon = SettingsIcon,
                size = ButtonSize.Small,
                isSelected = true
            )
            ActionButton(
                icon = SettingsIcon,
                isSelected = false,
                size = ButtonSize.Big,
                type = ButtonType.RoundedCorner
            )
        }

    }
}

@Composable
@FP6Preview()
fun ActionButton_LightPreview() {
    ActionButton_Preview()
}

@Composable
@FP6PreviewDark()
fun ActionButton_DarkPreview() {
    ActionButton_Preview()
}