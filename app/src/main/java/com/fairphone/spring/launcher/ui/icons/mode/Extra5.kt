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

val ModeIcons.Extra5: ImageVector
    get() {
        if (_Extra5 != null) {
            return _Extra5!!
        }
        _Extra5 = ImageVector.Builder(
            name = "Extra5",
            defaultWidth = 40.dp,
            defaultHeight = 40.dp,
            viewportWidth = 40f,
            viewportHeight = 40f
        ).apply {
            group(
                clipPathData = PathData {
                    moveTo(0f, 0f)
                    horizontalLineToRelative(40f)
                    verticalLineToRelative(40f)
                    horizontalLineToRelative(-40f)
                    close()
                }
            ) {
            }
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
                    moveTo(10f, 20f)
                    curveTo(15.523f, 20f, 20f, 15.523f, 20f, 10f)
                    curveTo(20f, 15.523f, 24.477f, 20f, 30f, 20f)
                    curveTo(24.477f, 20f, 20f, 24.477f, 20f, 30f)
                    curveTo(20f, 24.477f, 15.523f, 20f, 10f, 20f)
                    close()
                    moveTo(10f, 20f)
                    curveTo(4.477f, 20f, 0f, 24.477f, 0f, 30f)
                    curveTo(0f, 35.523f, 4.477f, 40f, 10f, 40f)
                    curveTo(15.523f, 40f, 20f, 35.523f, 20f, 30f)
                    curveTo(20f, 35.523f, 24.477f, 40f, 30f, 40f)
                    curveTo(35.523f, 40f, 40f, 35.523f, 40f, 30f)
                    curveTo(40f, 24.477f, 35.523f, 20f, 30f, 20f)
                    curveTo(35.523f, 20f, 40f, 15.523f, 40f, 10f)
                    curveTo(40f, 4.477f, 35.523f, 0f, 30f, 0f)
                    curveTo(24.477f, 0f, 20f, 4.477f, 20f, 10f)
                    curveTo(20f, 4.477f, 15.523f, 0f, 10f, 0f)
                    curveTo(4.477f, 0f, 0f, 4.477f, 0f, 10f)
                    curveTo(0f, 15.523f, 4.477f, 20f, 10f, 20f)
                    close()
                }
            }
        }.build()

        return _Extra5!!
    }

@Suppress("ObjectPropertyName")
private var _Extra5: ImageVector? = null
