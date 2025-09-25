/*
 * Copyright (C) 2025 FairPhone B.V.
 *
 * SPDX-FileCopyrightText: 2025. FairPhone B.V.
 *
 * SPDX-License-Identifier: EUPL-1.2
 */

package com.fairphone.spring.launcher.analytics

import androidx.compose.runtime.compositionLocalOf

/**
 * CompositionLocal to provide an [AnalyticsService] instance throughout the Composable tree.
 *
 * By default, a no-op implementation of [AnalyticsService] is provided.
 */
val LocalAnalyticsService = compositionLocalOf<AnalyticsService> {
    // Provide a no-op implementation as a default
    object : AnalyticsService {
        override fun trackEvent(event: AnalyticsEvent) {}
    }
}
