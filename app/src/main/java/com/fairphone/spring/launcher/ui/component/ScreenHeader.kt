package com.fairphone.spring.launcher.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fairphone.spring.launcher.R
import com.fairphone.spring.launcher.ui.FP6Preview
import com.fairphone.spring.launcher.ui.FP6PreviewDark
import com.fairphone.spring.launcher.ui.theme.FairphoneTypography
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme

@Composable
fun ScreenHeader(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = FairphoneTypography.H2
) {
    Text(
        text = text,
        style = style,
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Center,
        modifier = modifier.padding(16.dp)
    )
}

@Composable
private fun ModeSwitcherHeader_Preview() {
    SpringLauncherTheme {
        ScreenHeader(stringResource(R.string.mode_switcher_screen_header))
    }
}

@Composable
@FP6Preview()
private fun ModeSwitcherHeader_LightPreview() {
    ModeSwitcherHeader_Preview()
}

@Composable
@FP6PreviewDark()
private fun ModeSwitcherHeader_DarkPreview() {
    ModeSwitcherHeader_Preview()
}