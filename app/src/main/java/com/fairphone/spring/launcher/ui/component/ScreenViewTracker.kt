/*
 * Copyright (C) 2025 FairPhone B.V.
 *
 * SPDX-FileCopyrightText: 2025. FairPhone B.V.
 *
 * SPDX-License-Identifier: EUPL-1.2
 */

package com.fairphone.spring.launcher.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.fairphone.spring.launcher.analytics.AnalyticsEvent
import com.fairphone.spring.launcher.analytics.LocalAnalyticsService

/**
 * A composable function that tracks screen views using the provided [NavHostController].
 * It listens to destination changes in the navigation controller and logs screen views
 * to the analytics service.
 *
 * @param navController The [NavHostController] to observe for destination changes.
 */
@Composable
fun ScreenViewTracker(navController: NavHostController) {
    val analyticsService = LocalAnalyticsService.current
    DisposableEffect(navController) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            destination.route?.let { route ->
                val screenName = route.substringAfterLast('.')
                analyticsService.trackEvent(AnalyticsEvent.ScreenView(screenName))
            }
        }
        navController.addOnDestinationChangedListener(listener)
        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }
}
