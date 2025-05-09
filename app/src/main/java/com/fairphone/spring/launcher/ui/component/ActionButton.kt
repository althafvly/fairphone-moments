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
import androidx.compose.foundation.layout.padding
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
import com.fairphone.spring.launcher.ui.FP6Preview
import com.fairphone.spring.launcher.ui.FP6PreviewDark
import com.fairphone.spring.launcher.ui.modeicons.SettingsIcon
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
import com.fairphone.spring.launcher.ui.theme.selectedActionButtonStrokeDark
import com.fairphone.spring.launcher.ui.theme.selectedActionButtonStrokeEndGradientDark
import com.fairphone.spring.launcher.ui.theme.selectedActionButtonStrokeEndGradientLight


@Composable
fun ActionButton(
    icon: ImageVector,
    description: String,
    modifier: Modifier = Modifier,
    displayLabel: Boolean = false,
    isSelected: Boolean = false,
    onClick: () -> Unit = {}
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        val interactionSource = remember { MutableInteractionSource() }
        val isDark = isSystemInDarkTheme()
        val isPressed by interactionSource.collectIsPressedAsState()
        val isFocus by interactionSource.collectIsFocusedAsState()
        val isHover by interactionSource.collectIsHoveredAsState()

        Button(
            shape = RoundedCornerShape(50.dp),
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
            border = computeButtonBorder(
                isDark = isDark,
                isFocus = isFocus || isHover,
                isSelected = isSelected
            ),
            contentPadding = PaddingValues(12.dp),
            modifier = Modifier
                .computeButtonModifier(isPressed = isPressed, isDark = isDark)
                .width(48.dp)
                .height(48.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = description,
                tint = if (isSelected) onBackgroundLight else MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .width(24.dp)
                    .height(24.dp)
            )
        }
        if (displayLabel) {
            Text(
                text = description,
                style = FairphoneTypography.ButtonLegend,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
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

private fun computeButtonBackground(
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
    isSelected: Boolean
): BorderStroke {
    if (isFocus) {
        return BorderStroke(
            width = 1.dp,
            color = if (isDark) focusActionButtonStrokeDark else focusActionButtonStrokeLight
        )
    }

    val stroke = if (isDark) {
        if (isSelected) selectedActionButtonStrokeDark else actionButtonStrokeDark
    } else {
        if (isSelected) selectedActionButtonStrokeDark else actionButtonStrokeLight
    }
    return BorderStroke(
        width = 1.dp,
        brush = Brush.horizontalGradient(
            listOf(
                stroke,
                if (isDark) selectedActionButtonStrokeEndGradientDark else selectedActionButtonStrokeEndGradientLight
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
                description = "Add Moment",
                displayLabel = true
            )
            ActionButton(
                icon = SettingsIcon,
                description = "Update moment",
                isSelected = false
            )
            ActionButton(
                icon = SettingsIcon,
                description = "Update selected moment",
                isSelected = true,
                displayLabel = true
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