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

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

/**
 * Interface for analytics services.
 */
interface AnalyticsService {
    /**
     * Tracks a screen view.
     *
     * @param routeName The name of the screen.
     */
    fun trackScreenView(routeName: String)
}

/**
 * Firebase implementation of [AnalyticsService].
 *
 * @property firebaseAnalytics The FirebaseAnalytics instance.
 */
class FirebaseAnalyticsService(private val firebaseAnalytics: FirebaseAnalytics) : AnalyticsService {
    /**
     * Tracks a screen view using Firebase Analytics.
     */
    override fun trackScreenView(routeName: String) {
        val bundle = Bundle().apply {
            putString(FirebaseAnalytics.Param.SCREEN_NAME, routeName)
            putString(FirebaseAnalytics.Param.SCREEN_CLASS, routeName)
        }
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }
}
