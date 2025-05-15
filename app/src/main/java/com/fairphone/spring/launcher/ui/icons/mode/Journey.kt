package com.fairphone.spring.launcher.ui.icons.mode

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.fairphone.spring.launcher.ui.icons.ModeIcons

val ModeIcons.Journey: ImageVector
    get() {
        if (_Journey != null) {
            return _Journey!!
        }
        _Journey = ImageVector.Builder(
            name = "Journey",
            defaultWidth = 40.dp,
            defaultHeight = 40.dp,
            viewportWidth = 40f,
            viewportHeight = 40f
        ).apply {
            path(fill = SolidColor(Color(0xFF000000))) {
                moveTo(33.193f, 26.807f)
                curveTo(32.1f, 27.901f, 30.327f, 27.901f, 29.233f, 26.807f)
                lineTo(24.406f, 21.98f)
                curveTo(23.312f, 20.886f, 23.312f, 19.114f, 24.406f, 18.02f)
                lineTo(29.233f, 13.193f)
                curveTo(30.327f, 12.099f, 32.1f, 12.099f, 33.193f, 13.193f)
                lineTo(38.021f, 18.02f)
                curveTo(39.114f, 19.114f, 39.114f, 20.886f, 38.021f, 21.98f)
                lineTo(33.193f, 26.807f)
                close()
                moveTo(10.767f, 26.807f)
                curveTo(9.674f, 27.901f, 7.901f, 27.901f, 6.808f, 26.807f)
                lineTo(1.98f, 21.98f)
                curveTo(0.887f, 20.886f, 0.887f, 19.114f, 1.98f, 18.02f)
                lineTo(6.808f, 13.193f)
                curveTo(7.901f, 12.099f, 9.674f, 12.099f, 10.767f, 13.193f)
                lineTo(15.595f, 18.02f)
                curveTo(16.688f, 19.114f, 16.688f, 20.886f, 15.595f, 21.98f)
                lineTo(10.767f, 26.807f)
                close()
                moveTo(21.98f, 38.02f)
                curveTo(20.887f, 39.114f, 19.114f, 39.114f, 18.021f, 38.02f)
                lineTo(13.193f, 33.193f)
                curveTo(12.099f, 32.099f, 12.099f, 30.326f, 13.193f, 29.233f)
                lineTo(18.021f, 24.405f)
                curveTo(19.114f, 23.312f, 20.887f, 23.312f, 21.98f, 24.405f)
                lineTo(26.808f, 29.233f)
                curveTo(27.901f, 30.326f, 27.901f, 32.099f, 26.808f, 33.193f)
                lineTo(21.98f, 38.02f)
                close()
                moveTo(21.98f, 15.595f)
                curveTo(20.887f, 16.688f, 19.114f, 16.688f, 18.021f, 15.595f)
                lineTo(13.193f, 10.767f)
                curveTo(12.099f, 9.674f, 12.099f, 7.901f, 13.193f, 6.807f)
                lineTo(18.021f, 1.98f)
                curveTo(19.114f, 0.886f, 20.887f, 0.886f, 21.98f, 1.98f)
                lineTo(26.808f, 6.807f)
                curveTo(27.901f, 7.901f, 27.901f, 9.674f, 26.808f, 10.767f)
                lineTo(21.98f, 15.595f)
                close()
            }
        }.build()

        return _Journey!!
    }

@Suppress("ObjectPropertyName")
private var _Journey: ImageVector? = null
