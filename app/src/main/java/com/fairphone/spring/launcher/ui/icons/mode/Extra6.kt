/*
 * Copyright (C) 2025 FairPhone B.V.
 *
 * SPDX-FileCopyrightText: 2025. FairPhone B.V.
 *
 * SPDX-License-Identifier: EUPL-1.2
 */

package com.fairphone.spring.launcher.ui.icons.mode

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.fairphone.spring.launcher.ui.icons.ModeIcons

val ModeIcons.Extra6: ImageVector
    get() {
        if (_Extra6 != null) {
            return _Extra6!!
        }
        _Extra6 = ImageVector.Builder(
            name = "Extra6",
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
                path(fill = SolidColor(Color(0xFF141414))) {
                    moveTo(20.051f, 40f)
                    curveTo(19.42f, 40f, 18.887f, 39.543f, 18.734f, 38.931f)
                    curveTo(18.247f, 36.981f, 17.315f, 34.906f, 15.938f, 32.708f)
                    curveTo(14.306f, 30.069f, 11.979f, 27.622f, 8.958f, 25.365f)
                    curveTo(6.331f, 23.379f, 3.704f, 22.025f, 1.076f, 21.302f)
                    curveTo(0.454f, 21.131f, 0f, 20.579f, 0f, 19.934f)
                    curveTo(0f, 19.301f, 0.437f, 18.756f, 1.045f, 18.582f)
                    curveTo(3.621f, 17.847f, 6.102f, 16.653f, 8.49f, 15f)
                    curveTo(11.233f, 13.09f, 13.524f, 10.799f, 15.365f, 8.125f)
                    curveTo(16.993f, 5.743f, 18.11f, 3.388f, 18.717f, 1.058f)
                    curveTo(18.876f, 0.448f, 19.413f, 0f, 20.044f, 0f)
                    curveTo(20.682f, 0f, 21.223f, 0.458f, 21.378f, 1.077f)
                    curveTo(21.728f, 2.473f, 22.276f, 3.902f, 23.021f, 5.365f)
                    curveTo(23.958f, 7.17f, 25.156f, 8.906f, 26.615f, 10.573f)
                    curveTo(28.108f, 12.205f, 29.774f, 13.681f, 31.615f, 15f)
                    curveTo(34.019f, 16.705f, 36.464f, 17.902f, 38.949f, 18.593f)
                    curveTo(39.558f, 18.762f, 40f, 19.305f, 40f, 19.938f)
                    curveTo(40f, 20.58f, 39.545f, 21.127f, 38.926f, 21.297f)
                    curveTo(37.351f, 21.728f, 35.73f, 22.425f, 34.063f, 23.385f)
                    curveTo(32.049f, 24.566f, 30.174f, 25.972f, 28.437f, 27.604f)
                    curveTo(26.701f, 29.201f, 25.278f, 30.885f, 24.167f, 32.656f)
                    curveTo(22.787f, 34.859f, 21.853f, 36.949f, 21.367f, 38.928f)
                    curveTo(21.217f, 39.542f, 20.683f, 40f, 20.051f, 40f)
                    close()
                }
            }
        }.build()

        return _Extra6!!
    }

@Suppress("ObjectPropertyName")
private var _Extra6: ImageVector? = null
