package com.fairphone.spring.launcher.ui.screen.mode.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fairphone.spring.launcher.R
import com.fairphone.spring.launcher.data.model.LauncherProfile
import com.fairphone.spring.launcher.data.model.Presets
import com.fairphone.spring.launcher.ui.FP6Preview
import com.fairphone.spring.launcher.ui.FP6PreviewDark
import com.fairphone.spring.launcher.ui.component.ActionButton
import com.fairphone.spring.launcher.ui.modeicons.SettingsIcon
import com.fairphone.spring.launcher.ui.modeicons.fromString
import com.fairphone.spring.launcher.ui.theme.FairphoneTypography
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme
import com.fairphone.spring.launcher.ui.theme.actionButtonStrokeDark
import com.fairphone.spring.launcher.ui.theme.actionButtonStrokeLight
import com.fairphone.spring.launcher.ui.theme.focusActionButtonStrokeDark
import com.fairphone.spring.launcher.ui.theme.focusActionButtonStrokeLight
import com.fairphone.spring.launcher.ui.theme.modeButtonBackgroundDark
import com.fairphone.spring.launcher.ui.theme.modeButtonBackgroundLight
import com.fairphone.spring.launcher.ui.theme.onBackgroundLight
import com.fairphone.spring.launcher.ui.theme.pressedActionButtonEndGradienDark
import com.fairphone.spring.launcher.ui.theme.pressedActionButtonEndGradienLight
import com.fairphone.spring.launcher.ui.theme.pressedActionButtonStartGradienDark
import com.fairphone.spring.launcher.ui.theme.pressedActionButtonStartGradienLight
import com.fairphone.spring.launcher.ui.theme.selectedActionButtonStrokeDark
import com.fairphone.spring.launcher.ui.theme.selectedActionButtonStrokeEndGradientDark
import com.fairphone.spring.launcher.ui.theme.selectedActionButtonStrokeEndGradientLight

@Composable
fun ModeSwitcherButton(
    profile: LauncherProfile,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onModeSettingsClick: (LauncherProfile) -> Unit = {},
    onClick: () -> Unit = {},
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isDark = isSystemInDarkTheme()
    val isPressed by interactionSource.collectIsPressedAsState()
    val isFocus by interactionSource.collectIsFocusedAsState()

    Button(
        onClick = onClick,
        border = computeButtonBorder(
            isDark = isDark,
            isSelected = isSelected,
            isFocus = isFocus
        ),
        interactionSource = interactionSource,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = computeButtonBackground(
                isDark = isDark,
                isSelected = isSelected,
                isPressed = isPressed
            ),
            contentColor = if (isSelected) onBackgroundLight else MaterialTheme.colorScheme.onBackground,
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
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp),
        ) {
            Icon(
                imageVector = ImageVector.fromString(profile.icon),
                contentDescription = profile.name,
                modifier = Modifier

                    .padding(start = 16.dp, end = 16.dp)
                    .size(20.dp)
            )

            Text(
                text = profile.name,
                style = FairphoneTypography.H5,
            )

            Spacer(modifier = Modifier.weight(1f))

            ActionButton(
                icon = SettingsIcon,
                description = stringResource(R.string.mode_switcher_screen_settings),
                isSelected = isSelected,
            ) {
                onModeSettingsClick(profile)
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

private fun computeButtonBackground(
    isDark: Boolean,
    isSelected: Boolean,
    isPressed: Boolean
): Color {
    if (isSelected) {
        return Color.White
    }
    if (isPressed) {
        return Color.Transparent
    }
    return if (isDark) modeButtonBackgroundDark else modeButtonBackgroundLight
}

private fun computeButtonBorder(
    isDark: Boolean,
    isFocus: Boolean,
    isSelected: Boolean
): BorderStroke {
    BorderStroke(1.dp, Color.White)
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
fun ModeSwitcherButton_Preview() {
    SpringLauncherTheme {
        Column {
            ModeSwitcherButton(profile = Presets.Essentials, isSelected = true)
            ModeSwitcherButton(profile = Presets.Journey, isSelected = false)
        }

    }
}

@Composable
@FP6Preview()
fun ModeSwitcherButton_LightPreview() {
    ModeSwitcherButton_Preview()
}

@Composable
@FP6PreviewDark()
fun ModeSwitcherButton_DarkPreview() {
    ModeSwitcherButton_Preview()
}