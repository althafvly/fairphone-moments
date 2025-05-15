package com.fairphone.spring.launcher.ui.icons.mode

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.fairphone.spring.launcher.ui.icons.ModeIcons

val ModeIcons.Extra3: ImageVector
    get() {
        if (_Extra3 != null) {
            return _Extra3!!
        }
        _Extra3 = ImageVector.Builder(
            name = "Extra3",
            defaultWidth = 40.dp,
            defaultHeight = 40.dp,
            viewportWidth = 40f,
            viewportHeight = 40f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(25.721f, 3.267f)
                curveTo(23.166f, -1.089f, 16.868f, -1.089f, 14.312f, 3.267f)
                lineTo(14.219f, 3.425f)
                curveTo(13.04f, 5.435f, 10.891f, 6.676f, 8.561f, 6.692f)
                lineTo(8.378f, 6.693f)
                curveTo(3.328f, 6.729f, 0.179f, 12.182f, 2.673f, 16.574f)
                lineTo(2.764f, 16.733f)
                curveTo(3.915f, 18.759f, 3.915f, 21.241f, 2.764f, 23.267f)
                lineTo(2.673f, 23.426f)
                curveTo(0.179f, 27.818f, 3.328f, 33.271f, 8.378f, 33.307f)
                lineTo(8.561f, 33.308f)
                curveTo(10.891f, 33.324f, 13.04f, 34.565f, 14.219f, 36.575f)
                lineTo(14.312f, 36.733f)
                curveTo(16.868f, 41.089f, 23.166f, 41.089f, 25.721f, 36.733f)
                lineTo(25.814f, 36.575f)
                curveTo(26.993f, 34.565f, 29.143f, 33.324f, 31.472f, 33.308f)
                lineTo(31.656f, 33.307f)
                curveTo(36.706f, 33.271f, 39.855f, 27.818f, 37.36f, 23.426f)
                lineTo(37.27f, 23.267f)
                curveTo(36.119f, 21.241f, 36.119f, 18.759f, 37.27f, 16.733f)
                lineTo(37.36f, 16.574f)
                curveTo(39.855f, 12.182f, 36.706f, 6.729f, 31.656f, 6.693f)
                lineTo(31.472f, 6.692f)
                curveTo(29.143f, 6.676f, 26.993f, 5.435f, 25.814f, 3.425f)
                lineTo(25.721f, 3.267f)
                close()
                moveTo(20.017f, 29.921f)
                curveTo(25.496f, 29.921f, 29.938f, 25.479f, 29.938f, 20f)
                curveTo(29.938f, 14.521f, 25.496f, 10.079f, 20.017f, 10.079f)
                curveTo(14.538f, 10.079f, 10.096f, 14.521f, 10.096f, 20f)
                curveTo(10.096f, 25.479f, 14.538f, 29.921f, 20.017f, 29.921f)
                close()
            }
        }.build()

        return _Extra3!!
    }

@Suppress("ObjectPropertyName")
private var _Extra3: ImageVector? = null
