package com.fairphone.spring.launcher.ui.modeicons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.fairphone.spring.launcher.ui.ModeIcons

val ModeIcons.DeepFocus: ImageVector
    get() {
        if (_DeepFocus != null) {
            return _DeepFocus!!
        }
        _DeepFocus = ImageVector.Builder(
            name = "DeepFocus",
            defaultWidth = 39.dp,
            defaultHeight = 40.dp,
            viewportWidth = 39f,
            viewportHeight = 40f
        ).apply {
            group(
                clipPathData = PathData {
                    moveTo(39f, 0f)
                    horizontalLineTo(0f)
                    verticalLineTo(39f)
                    horizontalLineTo(39f)
                    verticalLineTo(0f)
                    close()
                }
            ) {
                path(fill = SolidColor(Color(0xFF000000))) {
                    moveTo(24.052f, 39f)
                    curveTo(19.5f, 39f, 19.5f, 28.074f, 14.948f, 28.074f)
                    curveTo(9.708f, 28.074f, 0f, 29.29f, 0f, 24.051f)
                    curveTo(0f, 19.499f, 10.925f, 19.499f, 10.925f, 14.947f)
                    curveTo(10.925f, 9.71f, 9.708f, 0f, 14.948f, 0f)
                    curveTo(19.5f, 0f, 19.5f, 10.926f, 24.052f, 10.926f)
                    curveTo(29.292f, 10.926f, 39f, 9.71f, 39f, 14.947f)
                    curveTo(39f, 19.499f, 28.073f, 19.499f, 28.073f, 24.051f)
                    curveTo(28.073f, 29.29f, 29.292f, 39f, 24.052f, 39f)
                    close()
                }
            }
        }.build()

        return _DeepFocus!!
    }

@Suppress("ObjectPropertyName")
private var _DeepFocus: ImageVector? = null
