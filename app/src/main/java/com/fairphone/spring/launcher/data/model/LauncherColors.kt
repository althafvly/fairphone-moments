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

package com.fairphone.spring.launcher.data.model

/**
 * Data class that represents the colors used in the launcher.
 *
 * @property rightColor The main color used in the launcher.
 * @property leftColor The secondary color used in the launcher.
 */
data class LauncherColors(
    val rightColor: Long,
    val leftColor: Long,
) {
    companion object {
        val Default = LauncherColors(rightColor = 0xB2FFBA63, leftColor = 0xB2C3D1D0)
        val Custom = LauncherColors(rightColor = 0xFFF26E6E, leftColor = 0xFFF27696)
        val DeepFocus = LauncherColors(rightColor = 0xB282C9F1, leftColor = 0xB2CBCEEA)
        val Journey = LauncherColors(rightColor = 0xB2F7CAC9, leftColor = 0xB2E5D1F8)
        val Recharge = LauncherColors(rightColor = 0xB2D8FF4F, leftColor = 0xB2BBD9D6)
        val QualityTime = LauncherColors(rightColor = 0xB2C0AFFF, leftColor = 0xB2B0CCD8)

        val All = listOf(
            Default,
            Custom,
            DeepFocus,
            Journey,
            Recharge,
            QualityTime,
        )
    }
}

