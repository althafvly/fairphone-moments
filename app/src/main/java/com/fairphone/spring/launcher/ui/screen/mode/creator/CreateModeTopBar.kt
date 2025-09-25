/*
 * Copyright (C) 2025 FairPhone B.V.
 *
 * SPDX-FileCopyrightText: 2025. FairPhone B.V.
 *
 * SPDX-License-Identifier: EUPL-1.2
 */

package com.fairphone.spring.launcher.ui.screen.mode.creator

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
                    size = ButtonSize.Small,
                    onClick = onNavigateBack
                )
            }
        },
        actions = {
            ActionButton(
                icon = NavIcons.Close,
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
@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_NO)
fun CreateMomentTopBar_LightPreview() {
    CreateMomentTopBar_Preview()
}

@Composable
@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
fun CreateMomentTopBar_DarkPreview() {
    CreateMomentTopBar_Preview()
}

@Composable
@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_NO)
fun CreateMomentTopBarWithBack_LightPreview() {
    CreateMomentTopBar_Preview(true)
}

@Composable
@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
fun CreateMomentTopBarWithBack_DarkPreview() {
    CreateMomentTopBar_Preview(true)
}