/*
 * Copyright (C) 2025 FairPhone B.V.
 *
 * SPDX-FileCopyrightText: 2025. FairPhone B.V.
 *
 * SPDX-License-Identifier: EUPL-1.2
 */

package com.fairphone.spring.launcher.analytics

import com.google.firebase.analytics.FirebaseAnalytics

/**
 * Interface for analytics services.
 */
interface AnalyticsService {
    /**
     * Tracks a generic analytics event.
     *
     * @param event The event to track.
     */

    fun trackEvent(event: AnalyticsEvent)
}

/**
 * Firebase implementation of [AnalyticsService].
 *
 * @property firebaseAnalytics The FirebaseAnalytics instance.
 */
class FirebaseAnalyticsService(private val firebaseAnalytics: FirebaseAnalytics) : AnalyticsService {
    /**
     * Tracks a generic analytics event using Firebase Analytics.
     *
     * @param event The event to track.
     */
    override fun trackEvent(event: AnalyticsEvent) {
        firebaseAnalytics.logEvent(event.name, event.params)
    }
}
