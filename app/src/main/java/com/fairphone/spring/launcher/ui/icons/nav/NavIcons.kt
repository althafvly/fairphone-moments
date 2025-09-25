/*
 * Copyright (C) 2025 FairPhone B.V.
 *
 * SPDX-FileCopyrightText: 2025. FairPhone B.V.
 *
 * SPDX-License-Identifier: EUPL-1.2
 */

package com.fairphone.spring.launcher.ui.icons.nav

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.fairphone.spring.launcher.ui.FP6Preview
import com.fairphone.spring.launcher.ui.FP6PreviewDark
import com.fairphone.spring.launcher.ui.icons.NavIcons
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme

enum class NavIcon(val imageVector: ImageVector) {
    NavBack(NavIcons.ArrowLeft),
    Close(NavIcons.Close),
    NavEnter(NavIcons.ChevronRight)
}

@Composable
fun NavIcon_Preview() {
    SpringLauncherTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NavIcon.entries.forEach {
                Text("Icon : ${it.name}", color = MaterialTheme.colorScheme.onBackground)
                Image(
                    imageVector = it.imageVector,
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                )
            }
        }
    }
}

@Composable
@FP6Preview()
fun NavIcon_LightPreview() {
    NavIcon_Preview()
}

@Composable
@FP6PreviewDark()
fun NavIcon_DarkPreview() {
    NavIcon_Preview()
}
