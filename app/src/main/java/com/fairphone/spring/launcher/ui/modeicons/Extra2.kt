package com.fairphone.spring.launcher.ui.modeicons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.fairphone.spring.launcher.ui.ModeIcons

val ModeIcons.Extra2: ImageVector
    get() {
        if (_Extra2 != null) {
            return _Extra2!!
        }
        _Extra2 = ImageVector.Builder(
            name = "Extra2",
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
                    moveTo(27.999f, 27.999f)
                    curveTo(44f, 44f, -4f, 44f, 12.001f, 27.999f)
                    curveTo(-4f, 44f, -4f, -4f, 12.001f, 12f)
                    curveTo(-4f, -4f, 44f, -4f, 27.999f, 12f)
                    curveTo(44f, -4f, 44f, 44f, 27.999f, 27.999f)
                    close()
                }
            }
        }.build()

        return _Extra2!!
    }

@Suppress("ObjectPropertyName")
private var _Extra2: ImageVector? = null
