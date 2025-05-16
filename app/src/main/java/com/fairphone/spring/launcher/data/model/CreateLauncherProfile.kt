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
 * Data Class used to build a new LauncherProfile.
 */
data class CreateLauncherProfile(
    val name: String,
    val icon: String,
    val bgColor1: Long,
    val bgColor2: Long,
    val visibleApps: List<String>,
    val allowedContacts: ContactType,
    val customContacts: List<String> = emptyList(),
    val repeatCallEnabled: Boolean,
    val wallpaperId: Int,
    val uiMode: UiMode,
    val blueLightFilterEnabled: Boolean,
    val soundSetting: SoundSetting,
    val batterySaverEnabled: Boolean,
    val reduceBrightnessEnabled: Boolean,
)
