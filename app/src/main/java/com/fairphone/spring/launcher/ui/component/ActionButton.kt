package com.fairphone.spring.launcher.ui.component

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fairphone.spring.launcher.ui.FP6Preview
import com.fairphone.spring.launcher.ui.FP6PreviewDark
import com.fairphone.spring.launcher.ui.modeicons.SettingsIcon
import com.fairphone.spring.launcher.ui.theme.FairphoneTypography
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme
import com.fairphone.spring.launcher.ui.theme.actionButtonBackground
import com.fairphone.spring.launcher.ui.theme.actionButtonStroke
import com.fairphone.spring.launcher.ui.theme.onBackgroundLight
import com.fairphone.spring.launcher.ui.theme.selectedActionButtonBackground
import com.fairphone.spring.launcher.ui.theme.selectedActionButtonStroke
import com.fairphone.spring.launcher.ui.theme.selectedActionButtonStrokeEndGradient

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
        val background = if (isSelected) selectedActionButtonBackground else actionButtonBackground
        val stroke = if (isSelected) selectedActionButtonStroke else actionButtonStroke

        Button(
            shape = RoundedCornerShape(50.dp),
            onClick = { onClick() },
            colors = ButtonDefaults.textButtonColors(
                containerColor = background,
                contentColor = MaterialTheme.colorScheme.onBackground
            ),
            border = BorderStroke(
                width = 2.dp,
                brush = Brush.horizontalGradient(
                    listOf(
                        stroke,
                        selectedActionButtonStrokeEndGradient
                    )
                )
            ),
            contentPadding = PaddingValues(12.dp),
            modifier = Modifier
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
                description = "Update moment",
                isSelected = true
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