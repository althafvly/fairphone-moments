package com.fairphone.spring.launcher.ui.modeicons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val SettingsIcon: ImageVector
    get() {
        if (_Settings != null) {
            return _Settings!!
        }
        _Settings = ImageVector.Builder(
            name = "Settings",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            group(
                clipPathData = PathData {
                    moveTo(0f, 0f)
                    horizontalLineToRelative(24f)
                    verticalLineToRelative(24f)
                    horizontalLineToRelative(-24f)
                    close()
                }
            ) {
                path(fill = SolidColor(Color(0xFF1C1B1F))) {
                    moveTo(9f, 11f)
                    horizontalLineTo(7f)
                    lineTo(7f, 8f)
                    lineTo(3f, 8f)
                    lineTo(3f, 6f)
                    lineTo(7f, 6f)
                    lineTo(7f, 3f)
                    horizontalLineTo(9f)
                    lineTo(9f, 11f)
                    close()
                    moveTo(21f, 8f)
                    lineTo(11f, 8f)
                    verticalLineTo(6f)
                    lineTo(21f, 6f)
                    verticalLineTo(8f)
                    close()
                    moveTo(21f, 18f)
                    horizontalLineTo(17f)
                    verticalLineTo(21f)
                    horizontalLineTo(15f)
                    lineTo(15f, 13f)
                    horizontalLineTo(17f)
                    verticalLineTo(16f)
                    lineTo(21f, 16f)
                    verticalLineTo(18f)
                    close()
                    moveTo(13f, 18f)
                    lineTo(3f, 18f)
                    lineTo(3f, 16f)
                    lineTo(13f, 16f)
                    lineTo(13f, 18f)
                    close()
                }
            }
        }.build()

        return _Settings!!
    }

@Suppress("ObjectPropertyName")
private var _Settings: ImageVector? = null
