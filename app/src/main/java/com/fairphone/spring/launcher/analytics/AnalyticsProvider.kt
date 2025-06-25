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
        override fun trackScreenView(routeName: String) {}
    }
}
