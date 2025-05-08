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

val ModeIcons.Balance: ImageVector
    get() {
        if (_Balance != null) {
            return _Balance!!
        }
        _Balance = ImageVector.Builder(
            name = "Balance",
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
                    fill = SolidColor(Color(0xFF000000)),
                    pathFillType = PathFillType.EvenOdd
                ) {
                    moveTo(19.999f, 36.742f)
                    curveTo(21.356f, 38.371f, 22.713f, 40f, 25.428f, 40f)
                    curveTo(31.677f, 40f, 33.482f, 31.689f, 33.484f, 25.431f)
                    curveTo(33.484f, 22.717f, 35.113f, 21.36f, 36.742f, 20.002f)
                    curveTo(38.371f, 18.645f, 40f, 17.288f, 40f, 14.573f)
                    curveTo(40f, 8.323f, 31.677f, 6.515f, 25.43f, 6.515f)
                    curveTo(22.716f, 6.515f, 21.358f, 4.887f, 20.001f, 3.258f)
                    curveTo(18.644f, 1.629f, 17.287f, 0f, 14.572f, 0f)
                    curveTo(8.321f, 0f, 6.514f, 8.323f, 6.514f, 14.569f)
                    curveTo(6.514f, 17.283f, 4.885f, 18.64f, 3.257f, 19.998f)
                    curveTo(1.628f, 21.355f, 0f, 22.712f, 0f, 25.427f)
                    curveTo(0f, 31.677f, 8.321f, 33.485f, 14.57f, 33.485f)
                    curveTo(17.284f, 33.485f, 18.642f, 35.113f, 19.999f, 36.742f)
                    close()
                    moveTo(20f, 27.4f)
                    curveTo(24.087f, 27.4f, 27.4f, 24.087f, 27.4f, 20f)
                    curveTo(27.4f, 15.913f, 24.087f, 12.6f, 20f, 12.6f)
                    curveTo(15.913f, 12.6f, 12.6f, 15.913f, 12.6f, 20f)
                    curveTo(12.6f, 24.087f, 15.913f, 27.4f, 20f, 27.4f)
                    close()
                }
            }
        }.build()

        return _Balance!!
    }

@Suppress("ObjectPropertyName")
private var _Balance: ImageVector? = null
