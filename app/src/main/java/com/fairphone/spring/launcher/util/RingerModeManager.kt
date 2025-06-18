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
