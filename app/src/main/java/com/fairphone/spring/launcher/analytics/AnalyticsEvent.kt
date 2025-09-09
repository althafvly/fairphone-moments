/*
 * Copyright (C) 2025 FairPhone B.V.
 *
 * SPDX-FileCopyrightText: 2025. FairPhone B.V.
 *
 * SPDX-License-Identifier: EUPL-1.2
 */

package com.fairphone.spring.launcher.analytics

import android.os.Bundle
import com.fairphone.spring.launcher.data.model.Preset
import com.google.firebase.analytics.FirebaseAnalytics

/**
 * Interface for all analytics events.
 */
sealed interface Event {
    /** The name of the event. */
    val name: String
    /** The parameters of the event. */
    val params: Bundle
}

/**
 * Sealed class for all analytics events.
 */
sealed class AnalyticsEvent() : Event {
    data class ScreenView(val routeName: String) : AnalyticsEvent() {
        override val name: String
            /** The name of the screen view event. */
            get() = FirebaseAnalytics.Event.SCREEN_VIEW

        override val params: Bundle
            /** The parameters of the screen view event. */
            get() = Bundle().apply {
                putString(FirebaseAnalytics.Param.SCREEN_NAME, routeName)
                putString(FirebaseAnalytics.Param.SCREEN_CLASS, routeName)
            }
    }

    /**
     * Event for when a mode is created.
     */
    data class CreateModeEvent(
        val preset: Preset,
        val modeName: String,
        val modeId: String,
        val visibleApps: List<String>,
    ) : AnalyticsEvent() {
        override val name: String
            /** The name of the create mode event. */
            get() = EVENT_MODE_CREATION

        override val params: Bundle
            /** The parameters of the create mode event. */
            get() = Bundle().apply {
                putString(PARAM_PRESET, preset.name)
                putString(PARAM_MODE_NAME, modeName)
                putString(PARAM_MODE_ID, modeId)
                putString(PARAM_VISIBLE_APPS, visibleApps.joinToString(","))
            }

        companion object {
            private const val EVENT_MODE_CREATION = "create_mode"
            private const val PARAM_PRESET = "preset"
            private const val PARAM_MODE_NAME = "mode_name"
            private const val PARAM_MODE_ID = "mode_id"
            private const val PARAM_VISIBLE_APPS = "visible_apps"
        }
    }

    /**
     * Event for when a mode is switched on.
     */
    data class SwitchOnEvent(
        val modeName: String,
        val modeId: String,
        val visibleApps: List<String>,
        val timestamp: Long,
    ) : AnalyticsEvent() {
        override val name: String
            /** The name of the switch on event. */
            get() = EVENT_SWITCH_ON

        override val params: Bundle
            /** The parameters of the switch on event. */
            get() = Bundle().apply {
                putString(PARAM_MODE_NAME, modeName)
                putString(PARAM_MODE_ID, modeId)
                putString(PARAM_VISIBLE_APPS, visibleApps.joinToString(","))
                putLong(PARAM_TIMESTAMP, timestamp)
            }

        companion object {
            private const val EVENT_SWITCH_ON = "switch_on"
            private const val PARAM_MODE_NAME = "mode_name"
            private const val PARAM_MODE_ID = "mode_id"
            private const val PARAM_VISIBLE_APPS = "visible_apps"
            private const val PARAM_TIMESTAMP = "timestamp"
        }
    }

    /**
     * Event for when a mode is switched off.
     */
    data class SwitchOffEvent(
        val modeName: String,
        val modeId: String,
        val visibleApps: List<String>,
        val timestamp: Long,
    ) : AnalyticsEvent() {
        override val name: String
            /** The name of the switch off event. */
            get() = EVENT_SWITCH_OFF

        override val params: Bundle
            /** The parameters of the switch off event. */
            get() = Bundle().apply {
                putString(PARAM_MODE_NAME, modeName)
                putString(PARAM_MODE_ID, modeId)
                putString(PARAM_VISIBLE_APPS, visibleApps.joinToString(","))
                putLong(PARAM_TIMESTAMP, timestamp)
            }

        companion object {
            private const val EVENT_SWITCH_OFF = "switch_off"
            private const val PARAM_MODE_NAME = "mode_name"
            private const val PARAM_MODE_ID = "mode_id"
            private const val PARAM_VISIBLE_APPS = "visible_apps"
            private const val PARAM_TIMESTAMP = "timestamp"
        }
    }
}

