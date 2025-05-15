package com.fairphone.spring.launcher.ui.icons.mode

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.fairphone.spring.launcher.ui.icons.ModeIcons

val ModeIcons.Spring : ImageVector
    get() {
        if (_Spring != null) {
            return _Spring!!
        }
        _Spring = ImageVector.Builder(
            name = "Settings",
            defaultWidth = 40.dp,
            defaultHeight = 40.dp,
            viewportWidth = 40f,
            viewportHeight = 40f
        ).apply {
            group (
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
                    moveTo(20f, 40f)
                    curveTo(18.333f, 40f, 16.944f, 39.462f, 15.833f, 38.385f)
                    curveTo(14.722f, 37.309f, 14.167f, 36.007f, 14.167f, 34.479f)
                    curveTo(14.167f, 33.611f, 14.34f, 32.83f, 14.688f, 32.135f)
                    curveTo(15.035f, 31.441f, 15.642f, 30.677f, 16.51f, 29.844f)
                    curveTo(17.413f, 29.01f, 18.125f, 28.229f, 18.646f, 27.5f)
                    curveTo(19.201f, 26.736f, 19.479f, 26.076f, 19.479f, 25.521f)
                    verticalLineTo(23.646f)
                    curveTo(18.715f, 23.472f, 18.038f, 23.125f, 17.448f, 22.604f)
                    curveTo(16.892f, 22.049f, 16.528f, 21.389f, 16.354f, 20.625f)
                    horizontalLineTo(14.479f)
                    curveTo(13.889f, 20.625f, 13.194f, 20.903f, 12.396f, 21.458f)
                    curveTo(11.597f, 22.014f, 10.833f, 22.691f, 10.104f, 23.49f)
                    curveTo(9.375f, 24.288f, 8.646f, 24.878f, 7.917f, 25.26f)
                    curveTo(7.222f, 25.642f, 6.424f, 25.833f, 5.521f, 25.833f)
                    curveTo(3.958f, 25.833f, 2.639f, 25.278f, 1.563f, 24.167f)
                    curveTo(0.521f, 23.056f, 0f, 21.667f, 0f, 20f)
                    curveTo(0f, 18.333f, 0.521f, 16.944f, 1.563f, 15.833f)
                    curveTo(2.639f, 14.722f, 3.958f, 14.167f, 5.521f, 14.167f)
                    curveTo(6.979f, 14.167f, 8.229f, 14.688f, 9.271f, 15.729f)
                    curveTo(10.313f, 16.771f, 11.25f, 17.656f, 12.083f, 18.385f)
                    curveTo(12.917f, 19.115f, 13.715f, 19.479f, 14.479f, 19.479f)
                    horizontalLineTo(16.354f)
                    curveTo(16.528f, 18.681f, 16.892f, 18.021f, 17.448f, 17.5f)
                    curveTo(18.038f, 16.944f, 18.715f, 16.597f, 19.479f, 16.458f)
                    verticalLineTo(14.583f)
                    curveTo(19.479f, 13.681f, 18.785f, 12.535f, 17.396f, 11.146f)
                    lineTo(16.302f, 10.052f)
                    curveTo(14.878f, 8.628f, 14.167f, 7.118f, 14.167f, 5.521f)
                    curveTo(14.167f, 3.958f, 14.722f, 2.656f, 15.833f, 1.615f)
                    curveTo(16.979f, 0.538f, 18.368f, 0f, 20f, 0f)
                    curveTo(21.667f, 0f, 23.056f, 0.538f, 24.167f, 1.615f)
                    curveTo(25.278f, 2.691f, 25.833f, 3.993f, 25.833f, 5.521f)
                    curveTo(25.833f, 7.292f, 24.965f, 8.958f, 23.229f, 10.521f)
                    curveTo(21.493f, 12.118f, 20.625f, 13.472f, 20.625f, 14.583f)
                    verticalLineTo(16.458f)
                    curveTo(21.424f, 16.597f, 22.083f, 16.944f, 22.604f, 17.5f)
                    curveTo(23.16f, 18.021f, 23.507f, 18.681f, 23.646f, 19.479f)
                    horizontalLineTo(25.521f)
                    curveTo(26.701f, 19.479f, 28.056f, 18.594f, 29.583f, 16.823f)
                    curveTo(31.146f, 15.052f, 32.778f, 14.167f, 34.479f, 14.167f)
                    curveTo(36.042f, 14.167f, 37.344f, 14.74f, 38.385f, 15.885f)
                    curveTo(39.462f, 16.997f, 40f, 18.368f, 40f, 20f)
                    curveTo(40f, 21.667f, 39.462f, 23.056f, 38.385f, 24.167f)
                    curveTo(37.309f, 25.278f, 36.007f, 25.833f, 34.479f, 25.833f)
                    curveTo(33.021f, 25.833f, 31.788f, 25.33f, 30.781f, 24.323f)
                    curveTo(29.774f, 23.316f, 28.837f, 22.448f, 27.969f, 21.719f)
                    curveTo(27.101f, 20.99f, 26.285f, 20.625f, 25.521f, 20.625f)
                    horizontalLineTo(23.646f)
                    curveTo(23.368f, 22.292f, 22.361f, 23.299f, 20.625f, 23.646f)
                    verticalLineTo(25.521f)
                    curveTo(20.625f, 26.562f, 21.493f, 27.899f, 23.229f, 29.531f)
                    curveTo(24.965f, 31.163f, 25.833f, 32.812f, 25.833f, 34.479f)
                    curveTo(25.833f, 36.042f, 25.26f, 37.344f, 24.115f, 38.385f)
                    curveTo(23.003f, 39.462f, 21.632f, 40f, 20f, 40f)
                    close()
                }
            }
        }.build()

        return _Spring!!
    }

@Suppress("ObjectPropertyName")
private var _Spring: ImageVector? = null
