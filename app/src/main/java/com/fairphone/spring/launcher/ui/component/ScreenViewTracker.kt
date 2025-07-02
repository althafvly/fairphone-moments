/*
 * Copyright (c) 2025. Fairphone B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
