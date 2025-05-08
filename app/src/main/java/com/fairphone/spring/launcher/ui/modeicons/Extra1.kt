package com.fairphone.spring.launcher.ui.modeicons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.fairphone.spring.launcher.ui.ModeIcons

val ModeIcons.Extra1: ImageVector
    get() {
        if (_Extra1 != null) {
            return _Extra1!!
        }
        _Extra1 = ImageVector.Builder(
            name = "Extra1",
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
                    moveTo(12.266f, 19.999f)
                    curveTo(-10.943f, 37.096f, 2.913f, 50.952f, 20.004f, 27.739f)
                    curveTo(37.084f, 50.952f, 50.94f, 37.096f, 27.742f, 19.999f)
                    curveTo(50.951f, 2.905f, 37.095f, -10.954f, 20.004f, 12.262f)
                    curveTo(2.902f, -10.946f, -10.954f, 2.905f, 12.266f, 19.999f)
                    close()
                    moveTo(20f, 25f)
                    curveTo(22.761f, 25f, 25f, 22.761f, 25f, 20f)
                    curveTo(25f, 17.239f, 22.761f, 15f, 20f, 15f)
                    curveTo(17.239f, 15f, 15f, 17.239f, 15f, 20f)
                    curveTo(15f, 22.761f, 17.239f, 25f, 20f, 25f)
                    close()
                }
            }
        }.build()

        return _Extra1!!
    }

@Suppress("ObjectPropertyName")
private var _Extra1: ImageVector? = null
