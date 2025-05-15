package com.fairphone.spring.launcher.ui.icons.nav

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.fairphone.spring.launcher.ui.icons.NavIcons

val NavIcons.Close: ImageVector
    get() {
        if (_Close != null) {
            return _Close!!
        }
        _Close = ImageVector.Builder(
            name = "Close",
            defaultWidth = 16.dp,
            defaultHeight = 16.dp,
            viewportWidth = 16f,
            viewportHeight = 16f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF2E2E2E)),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(9.414f, 8f)
                lineTo(13.707f, 3.707f)
                lineTo(12.293f, 2.293f)
                lineTo(8f, 6.586f)
                lineTo(3.707f, 2.293f)
                lineTo(2.293f, 3.707f)
                lineTo(6.586f, 8f)
                lineTo(2.293f, 12.293f)
                lineTo(3.707f, 13.707f)
                lineTo(8f, 9.414f)
                lineTo(12.293f, 13.707f)
                lineTo(13.707f, 12.293f)
                lineTo(9.414f, 8f)
                close()
            }
        }.build()

        return _Close!!
    }

@Suppress("ObjectPropertyName")
private var _Close: ImageVector? = null
