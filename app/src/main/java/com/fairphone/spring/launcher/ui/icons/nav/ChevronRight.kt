package com.fairphone.spring.launcher.ui.icons.nav

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.fairphone.spring.launcher.ui.icons.NavIcons

val NavIcons.ChevronRight: ImageVector
    get() {
        if (_ChevronRight != null) {
            return _ChevronRight!!
        }
        _ChevronRight = ImageVector.Builder(
            name = "ChevronRight",
            defaultWidth = 16.dp,
            defaultHeight = 16.dp,
            viewportWidth = 16f,
            viewportHeight = 16f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF141414)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Square
            ) {
                moveTo(6f, 3.5f)
                lineTo(10.146f, 7.646f)
                curveTo(10.342f, 7.842f, 10.342f, 8.158f, 10.146f, 8.354f)
                lineTo(6f, 12.5f)
            }
        }.build()

        return _ChevronRight!!
    }

@Suppress("ObjectPropertyName")
private var _ChevronRight: ImageVector? = null
