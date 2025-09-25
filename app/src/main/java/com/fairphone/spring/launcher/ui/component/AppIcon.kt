/*
 * Copyright (C) 2025 FairPhone B.V.
 *
 * SPDX-FileCopyrightText: 2025. FairPhone B.V.
 *
 * SPDX-License-Identifier: EUPL-1.2
 */

package com.fairphone.spring.launcher.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.fairphone.spring.launcher.data.model.AppInfo

@Composable
fun AppIcon(appInfo: AppInfo, modifier: Modifier = Modifier) {
    AsyncImage(
        model = appInfo.icon,
        contentDescription = appInfo.name,
        contentScale = ContentScale.Fit,
        modifier = modifier,
    )
}
