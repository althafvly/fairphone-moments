/*
 * Copyright (C) 2025 FairPhone B.V.
 *
 * SPDX-FileCopyrightText: 2025. FairPhone B.V.
 *
 * SPDX-License-Identifier: EUPL-1.2
 */

package com.fairphone.spring.launcher.util

/**
 * A simple singleton object to manage saving and retrieving the ringer mode.
 * This ensures we can properly restore the user's setting after a filtered call.
 */
object RingerModeManager {

    private var originalRingerMode: Int? = null

    /**
     * Saves the current ringer mode.
     * @param mode The current ringer mode, e.g., AudioManager.RINGER_MODE_NORMAL.
     */
    fun saveCurrentRingerMode(mode: Int) {
        // Only save if we don't already have a saved mode. This prevents overwriting
        // the original state during complex call scenarios (e.g., call waiting).
        if (originalRingerMode == null) {
            originalRingerMode = mode
        }
    }

    /**
     * @return The original ringer mode that was saved, or null if none was saved.
     */
    fun getOriginalRingerMode(): Int? {
        return originalRingerMode
    }

    /**
     * Clears the saved ringer mode. This should be called after restoring the state.
     */
    fun clearRingerMode() {
        originalRingerMode = null
    }
}
