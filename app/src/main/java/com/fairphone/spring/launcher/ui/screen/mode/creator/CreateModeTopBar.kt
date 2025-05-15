package com.fairphone.spring.launcher.ui.screen.mode.creator

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fairphone.spring.launcher.R
import com.fairphone.spring.launcher.ui.FP6Preview
import com.fairphone.spring.launcher.ui.FP6PreviewDark
import com.fairphone.spring.launcher.ui.component.ActionButton
import com.fairphone.spring.launcher.ui.component.ButtonSize
import com.fairphone.spring.launcher.ui.icons.NavIcons
import com.fairphone.spring.launcher.ui.icons.nav.ArrowLeft
import com.fairphone.spring.launcher.ui.icons.nav.Close
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateMomentTopBar(
    hasBackButton: Boolean,
    onNavigateBack: () -> Unit,
    onNavigateClose: () -> Unit
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
        ),
        title = {},
        navigationIcon = {
            if (hasBackButton) {
                ActionButton(
                    icon = NavIcons.ArrowLeft,
                    description = stringResource(R.string.bt_navigate_back),
                    size = ButtonSize.Small,
                    onClick = onNavigateBack
                )
            }
        },
        actions = {
            ActionButton(
                icon = NavIcons.Close,
                description = stringResource(R.string.bt_navigation_close),
                size = ButtonSize.Small,
                onClick = onNavigateClose,
            )
        },
        modifier = Modifier.padding(horizontal = 20.dp)
    )
}

@Composable
fun CreateMomentTopBar_Preview(withBack: Boolean = false) {
    SpringLauncherTheme {
        CreateMomentTopBar(withBack, {}, {})
    }
}

@Composable
@FP6Preview()
fun CreateMomentTopBar_LightPreview() {
    CreateMomentTopBar_Preview()
}

@Composable
@FP6PreviewDark()
fun CreateMomentTopBar_DarkPreview() {
    CreateMomentTopBar_Preview()
}

@Composable
@FP6Preview()
fun CreateMomentTopBarWithBack_LightPreview() {
    CreateMomentTopBar_Preview(true)
}

@Composable
@FP6PreviewDark()
fun CreateMomentTopBarWithBack_DarkPreview() {
    CreateMomentTopBar_Preview(true)
}