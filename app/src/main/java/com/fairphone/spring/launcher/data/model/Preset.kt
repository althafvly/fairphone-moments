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

import android.content.Context
import com.fairphone.spring.launcher.R
import com.fairphone.spring.launcher.data.model.protos.LauncherProfile
import com.fairphone.spring.launcher.data.model.protos.LauncherProfileApp
import com.fairphone.spring.launcher.data.model.protos.launcherProfile
import com.fairphone.spring.launcher.data.model.protos.launcherProfileApp

const val PROFILE_ID_CUSTOM: String = "custom"

enum class Preset(
    val id: String,
    val title: Int,
    val subtitle: Int,
    val icon: String,
    val colors: LauncherColors,
    val defaultApps: List<AppPreset>
) {
    Custom(
        id = PROFILE_ID_CUSTOM,
        title = R.string.mode_title_custom,
        subtitle = R.string.mode_subtitle_custom,
        icon = "Extra6",
        colors = LauncherColors(mainColor = 0xFFF26E6E, secondaryColor = 0xFFF27696),
        defaultApps = emptyList()
    ),
    Essentials(
        id = "spring",
        title = R.string.mode_title_essential,
        subtitle = R.string.mode_subtitle_essential,
        icon = "Spring",
        colors = LauncherColors(mainColor = 0xB2FFBA63, secondaryColor = 0xB2C3D1D0),
        defaultApps = listOf(
            AppPreset(Camera),
            AppPreset(DefaultBrowser),
            AppPreset(GoogleMaps),
            AppPreset(Messages),
            AppPreset(Phone),
        )
    ),
    DeepFocus(
        id = "deep_focus",
        title = R.string.mode_title_deep_focus,
        subtitle = R.string.mode_subtitle_deep_focus,
        icon = "DeepFocus",
        colors = LauncherColors(mainColor = 0xFFF26E6E, secondaryColor = 0xFFF27696),
        defaultApps = listOf(
            AppPreset(GoogleGmail),
            AppPreset(GoogleCalendar),
            AppPreset(GoogleDrive),
            AppPreset(GoogleMeet),
            AppPreset(GoogleKeepNotes),
        )
    ),
    Journey(
        id = "journey",
        title = R.string.mode_title_journey,
        subtitle = R.string.mode_subtitle_journey,
        icon = "Journey",
        colors = LauncherColors(mainColor = 0xFF00433D, secondaryColor = 0xFFD8FF4F),
        defaultApps = listOf(
            AppPreset(Waze, alternatives = listOf(GoogleMaps)),
            AppPreset(Spotify, alternatives = listOf(Tidal, Deezer, GoogleYoutubeMusic)),
            AppPreset(Phone)
        )
    ),
    Recharge(
        id = "recharge",
        title = R.string.mode_title_recharge,
        subtitle = R.string.mode_subtitle_recharge,
        icon = "Recharge",
        colors = LauncherColors(mainColor = 0xFF2D9197, secondaryColor = 0xFF66A2DD),
        defaultApps = listOf(
            AppPreset(Spotify, alternatives = listOf(Tidal, Deezer, GoogleYoutubeMusic)),
            AppPreset(Headspace, alternatives = listOf(Calm, Clock)),
        )
    ),
    QualityTime(
        id = "qualitytime",
        title = R.string.mode_title_quality_time,
        subtitle = R.string.mode_subtitle_quality_time,
        icon = "QualityTime",
        colors = LauncherColors(mainColor = 0xFFD8FF4F, secondaryColor = 0xFF42CC60),
        defaultApps = listOf(
            AppPreset(Camera),
            AppPreset(GooglePhoto),
            AppPreset(Whatsapp, alternatives = listOf(Messages)),
        )
    );

    fun getVisibleAppPackageNames(context: Context): List<String> =
        defaultApps.map { app ->
            app.allApps.firstNotNullOf {
                it.getPackageName(context)
            }
        }

    fun getVisibleLauncherApps(context: Context): List<LauncherProfileApp> =
        defaultApps.map { app ->
            app.allApps.firstNotNullOf {
                launcherProfileApp {
                    packageName = it.getPackageName(context)
                    isWorkApp = it.isWorkApp
                }
            }
        }

    fun getVisibleAppInfos(
        context: Context,
        installedApps: List<AppInfo> = emptyList()
    ): List<AppInfo> =
        defaultApps.map { app ->
            app.allApps
                .map {
                    it.getPackageName(context)
                }.firstNotNullOf { packageName ->
                    installedApps.firstOrNull { it.packageName == packageName }
                }
        }
}

val Mock_Profile = launcherProfile {
    id = "spring"
    name = "Spring"
    icon = "Spring"
    bgColor1 = 0xB2C3D1D0
    bgColor2 = 0xB2FFBA63
}

fun LauncherProfile.colors(): LauncherColors {
    return LauncherColors(
        mainColor = bgColor2,
        secondaryColor = bgColor1

    )
}