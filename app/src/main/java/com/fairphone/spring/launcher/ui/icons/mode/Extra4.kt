/*
 * Copyright (C) 2025 FairPhone B.V.
 *
 * SPDX-FileCopyrightText: 2025. FairPhone B.V.
 *
 * SPDX-License-Identifier: EUPL-1.2
 */

package com.fairphone.spring.launcher.ui.icons.mode

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.fairphone.spring.launcher.ui.icons.ModeIcons

val ModeIcons.Extra4: ImageVector
    get() {
        if (_Extra4 != null) {
            return _Extra4!!
        }
        _Extra4 = ImageVector.Builder(
            name = "Extra4",
            defaultWidth = 40.dp,
            defaultHeight = 40.dp,
            viewportWidth = 40f,
            viewportHeight = 40f
        ).apply {
            group(
                clipPathData = PathData {
                    moveTo(40f, 0f)
                    horizontalLineTo(0f)
                    verticalLineTo(40f)
                    horizontalLineTo(40f)
                    verticalLineTo(0f)
                    close()
                }
            ) {
                path(
                    fill = SolidColor(Color(0xFF141414)),
                    pathFillType = PathFillType.EvenOdd
                ) {
                    moveTo(10.143f, 0f)
                    horizontalLineTo(0.143f)
                    verticalLineTo(10f)
                    curveTo(0.143f, 15.092f, 3.949f, 19.296f, 8.872f, 19.92f)
                    curveTo(3.88f, 20.48f, 0f, 24.716f, 0f, 29.857f)
                    lineTo(0f, 39.857f)
                    horizontalLineTo(10f)
                    curveTo(15.092f, 39.857f, 19.296f, 36.051f, 19.92f, 31.128f)
                    curveTo(20.48f, 36.12f, 24.716f, 40f, 29.857f, 40f)
                    horizontalLineTo(39.857f)
                    verticalLineTo(30f)
                    curveTo(39.857f, 24.908f, 36.051f, 20.704f, 31.128f, 20.08f)
                    curveTo(36.12f, 19.52f, 40f, 15.284f, 40f, 10.143f)
                    verticalLineTo(0.143f)
                    lineTo(30f, 0.143f)
                    curveTo(24.908f, 0.143f, 20.704f, 3.949f, 20.08f, 8.872f)
                    curveTo(19.52f, 3.88f, 15.284f, 0f, 10.143f, 0f)
                    close()
                }
            }
        }.build()

        return _Extra4!!
    }

@Suppress("ObjectPropertyName")
private var _Extra4: ImageVector? = null
