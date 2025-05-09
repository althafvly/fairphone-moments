package com.fairphone.spring.launcher.ui.screen.mode.component

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.fairphone.spring.launcher.ui.theme.actionButtonBackground
import com.fairphone.spring.launcher.ui.theme.onBackgroundLight

@Composable
fun ModeSwitcherButton(
    profile: LauncherProfile,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    val colors = if (isSelected) {
        ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = onBackgroundLight,
        )
    } else {
        ButtonDefaults.buttonColors(
            containerColor = actionButtonBackground,
            contentColor = MaterialTheme.colorScheme.onBackground,
        )
    }
    Button(
        onClick = onClick,
        border = BorderStroke(1.dp, Color.White),
        shape = RoundedCornerShape(16.dp),
        colors = colors,
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
        modifier = modifier
            //.height(64.dp)
            .fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Icon(
                imageVector = ImageVector.fromString(profile.icon),
                contentDescription = profile.name,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp).size(20.dp)
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

            )
        }
    }
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