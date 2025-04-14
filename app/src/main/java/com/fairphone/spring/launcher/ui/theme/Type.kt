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

package com.fairphone.spring.launcher.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.fairphone.spring.launcher.R

val BricolageGrotesque = FontFamily(
    Font(R.font.bricolage_grotesque_light, FontWeight.Light),
    Font(R.font.bricolage_grotesque_regular, FontWeight.Normal),
    Font(R.font.bricolage_grotesque_medium, FontWeight.Medium),
    Font(R.font.bricolage_grotesque_bold, FontWeight.Bold),
)

val DmSans = FontFamily(
    Font(R.font.dm_sans_light, FontWeight.Light),
    Font(R.font.dm_sans_regular, FontWeight.Normal),
    Font(R.font.dm_sans_medium, FontWeight.Medium),
    Font(R.font.dm_sans_semibold, FontWeight.SemiBold),
    Font(R.font.dm_sans_bold, FontWeight.Bold),
)

val Inter = FontFamily(
    Font(R.font.inter_light, FontWeight.Light),
    Font(R.font.inter_regular, FontWeight.Normal),
    Font(R.font.inter_medium, FontWeight.Medium),
    Font(R.font.inter_semibold, FontWeight.SemiBold),
    Font(R.font.inter_bold, FontWeight.Bold),
)

object FairphoneTypography {
    // Headings
    val H4 = TextStyle(
        fontSize = 20.sp,
        lineHeight = 24.sp,
        fontFamily = DmSans,
        fontWeight = FontWeight.Medium,
    )
    // Body
    val BodyMedium = TextStyle(
        fontSize = 16.sp,
        lineHeight = 24.sp,
        fontFamily = DmSans,
        fontWeight = FontWeight.Medium,
    )
    val BodySmall = TextStyle(
        fontSize = 14.sp,
        lineHeight = 19.6.sp,
        fontFamily = DmSans,
        fontWeight = FontWeight.Normal,
    )
    // Labels
    val LabelMedium = TextStyle(
        fontSize = 12.sp,
        lineHeight = 16.8.sp,
        fontFamily = DmSans,
        fontWeight = FontWeight.Medium,
    )
    // Custom
    val Time = TextStyle(
        fontSize = 44.sp,
        lineHeight = 44.sp,
        fontFamily = BricolageGrotesque,
        fontWeight = FontWeight.Medium,
        textAlign = TextAlign.Center,
    )
    val Date = TextStyle(
        fontSize = 22.sp,
        lineHeight = 22.sp,
        fontFamily = BricolageGrotesque,
        fontWeight = FontWeight.Normal,
        textAlign = TextAlign.Center,
    )
    val ButtonDefault = TextStyle(
        fontSize = 18.sp,
        lineHeight = 18.sp,
        fontFamily = DmSans,
        fontWeight = FontWeight.Medium,
        textAlign = TextAlign.Center,
    )
    val AppButtonDefault = TextStyle(
        fontSize = 22.sp,
        lineHeight = 26.4.sp,
        fontFamily = DmSans,
        fontWeight = FontWeight.Medium,
        textAlign = TextAlign.Center,
    )
    val SwitchLabel = TextStyle(
        fontSize = 15.sp,
        lineHeight = 18.sp,
        fontFamily = DmSans,
        fontWeight = FontWeight.SemiBold,
    )


}


// Default Material 3 typography values
val baseline = Typography()

val LauncherTypography = Typography(
    displayLarge = baseline.displayLarge.copy(fontFamily = BricolageGrotesque),
    displayMedium = baseline.displayMedium.copy(fontFamily = BricolageGrotesque),
    displaySmall = baseline.displaySmall.copy(fontFamily = BricolageGrotesque),
    headlineLarge = baseline.headlineLarge.copy(fontFamily = BricolageGrotesque),
    headlineMedium = baseline.headlineMedium.copy(fontFamily = BricolageGrotesque),
    headlineSmall = baseline.headlineSmall.copy(fontFamily = BricolageGrotesque),
    titleLarge = baseline.titleLarge.copy(fontFamily = BricolageGrotesque),
    titleMedium = baseline.titleMedium.copy(fontFamily = BricolageGrotesque),
    titleSmall = baseline.titleSmall.copy(fontFamily = BricolageGrotesque),
    bodyLarge = baseline.bodyLarge.copy(fontFamily = DmSans),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = DmSans),
    bodySmall = baseline.bodySmall.copy(fontFamily = DmSans),
    labelLarge = baseline.labelLarge.copy(fontFamily = DmSans),
    labelMedium = baseline.labelMedium.copy(fontFamily = DmSans),
    labelSmall = baseline.labelSmall.copy(fontFamily = DmSans),
)
