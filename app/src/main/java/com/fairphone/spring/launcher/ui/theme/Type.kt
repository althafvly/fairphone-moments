/*
 * Copyright (C) 2025 FairPhone B.V.
 *
 * SPDX-FileCopyrightText: 2025. FairPhone B.V.
 *
 * SPDX-License-Identifier: EUPL-1.2
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

object FairphoneTypography {
    // Headings
    val H4 = TextStyle(
        fontSize = 20.sp,
        lineHeight = 24.sp,
        fontFamily = DmSans,
        fontWeight = FontWeight.Medium,
    )
    val H5 = TextStyle(
        fontSize = 18.sp,
        lineHeight = 21.6.sp,
        fontFamily = DmSans,
        fontWeight = FontWeight(500),
        letterSpacing = -0.36.sp
    )
    val H2 = TextStyle(
        fontSize = 28.sp,
        lineHeight = 33.6.sp,
        fontFamily = DmSans,
        fontWeight = FontWeight(500),
        letterSpacing = -0.56.sp
    )
    val H3 = TextStyle(
        fontSize = 22.sp,
        lineHeight = 26.4.sp,
        fontFamily = DmSans,
        fontWeight = FontWeight(500),
        letterSpacing = -0.44.sp
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
    val ButtonLegend = TextStyle(
        fontSize = 12.sp,
        lineHeight = 16.8.sp,
        fontFamily = DmSans,
        fontWeight = FontWeight.Medium,
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
    headlineMedium = FairphoneTypography.H4,
    headlineSmall = FairphoneTypography.H5,
    titleLarge = baseline.titleLarge.copy(fontFamily = BricolageGrotesque),
    titleMedium = baseline.titleMedium.copy(fontFamily = BricolageGrotesque),
    titleSmall = baseline.titleSmall.copy(fontFamily = BricolageGrotesque),
    bodyLarge = baseline.bodyLarge.copy(fontFamily = DmSans),
    bodyMedium = FairphoneTypography.BodyMedium,
    bodySmall = FairphoneTypography.BodySmall,
    labelLarge = baseline.labelLarge.copy(fontFamily = DmSans),
    labelMedium = FairphoneTypography.LabelMedium,
    labelSmall = baseline.labelSmall.copy(fontFamily = DmSans),
)
