package com.fairphone.spring.launcher.ui.modeicons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.fairphone.spring.launcher.ui.ModeIcons

val ModeIcons.Recharge: ImageVector
    get() {
        if (_Recharge != null) {
            return _Recharge!!
        }
        _Recharge = ImageVector.Builder(
            name = "Recharge",
            defaultWidth = 40.dp,
            defaultHeight = 40.dp,
            viewportWidth = 40f,
            viewportHeight = 40f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(26.178f, 9.413f)
                curveTo(26.529f, 8.592f, 26.724f, 7.686f, 26.724f, 6.734f)
                curveTo(26.724f, 3.015f, 23.754f, 0f, 20.09f, 0f)
                curveTo(16.427f, 0f, 13.457f, 3.015f, 13.457f, 6.734f)
                curveTo(13.457f, 7.686f, 13.652f, 8.592f, 14.003f, 9.413f)
                curveTo(12.798f, 7.729f, 10.841f, 6.633f, 8.633f, 6.633f)
                curveTo(4.97f, 6.633f, 2f, 9.648f, 2f, 13.367f)
                curveTo(2f, 16.688f, 4.369f, 19.448f, 7.485f, 20f)
                curveTo(4.369f, 20.552f, 2f, 23.312f, 2f, 26.633f)
                curveTo(2f, 30.352f, 4.97f, 33.367f, 8.633f, 33.367f)
                curveTo(10.841f, 33.367f, 12.798f, 32.271f, 14.003f, 30.587f)
                curveTo(13.652f, 31.408f, 13.457f, 32.314f, 13.457f, 33.266f)
                curveTo(13.457f, 36.985f, 16.427f, 40f, 20.09f, 40f)
                curveTo(23.754f, 40f, 26.724f, 36.985f, 26.724f, 33.266f)
                curveTo(26.724f, 32.314f, 26.529f, 31.408f, 26.178f, 30.587f)
                curveTo(27.383f, 32.271f, 29.339f, 33.367f, 31.548f, 33.367f)
                curveTo(35.211f, 33.367f, 38.181f, 30.352f, 38.181f, 26.633f)
                curveTo(38.181f, 23.312f, 35.812f, 20.552f, 32.696f, 20f)
                curveTo(35.812f, 19.448f, 38.181f, 16.688f, 38.181f, 13.367f)
                curveTo(38.181f, 9.648f, 35.211f, 6.633f, 31.548f, 6.633f)
                curveTo(29.339f, 6.633f, 27.383f, 7.729f, 26.178f, 9.413f)
                close()
                moveTo(25.461f, 29.313f)
                curveTo(25.109f, 28.492f, 24.915f, 27.586f, 24.915f, 26.633f)
                curveTo(24.915f, 23.312f, 27.283f, 20.552f, 30.4f, 20f)
                curveTo(27.283f, 19.448f, 24.915f, 16.688f, 24.915f, 13.367f)
                curveTo(24.915f, 12.415f, 25.109f, 11.508f, 25.461f, 10.687f)
                curveTo(24.255f, 12.372f, 22.299f, 13.467f, 20.09f, 13.467f)
                curveTo(17.882f, 13.467f, 15.926f, 12.372f, 14.72f, 10.687f)
                curveTo(15.072f, 11.508f, 15.266f, 12.415f, 15.266f, 13.367f)
                curveTo(15.266f, 16.688f, 12.898f, 19.448f, 9.781f, 20f)
                curveTo(12.898f, 20.552f, 15.266f, 23.312f, 15.266f, 26.633f)
                curveTo(15.266f, 27.586f, 15.072f, 28.492f, 14.72f, 29.313f)
                curveTo(15.926f, 27.628f, 17.882f, 26.533f, 20.09f, 26.533f)
                curveTo(22.299f, 26.533f, 24.255f, 27.628f, 25.461f, 29.313f)
                close()
            }
        }.build()

        return _Recharge!!
    }

@Suppress("ObjectPropertyName")
private var _Recharge: ImageVector? = null
