/*
 * Copyright (c) 2025. Fairphone B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License
 *
 *         at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fairphone.spring.launcher.ui.modeicons

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fairphone.spring.launcher.ui.ModeIcons
import kotlin.Unit

public val ModeIcons.Vector: ImageVector
    get() {
        if (com.fairphone.spring.launcher.ui.modeicons._vector != null) {
            return com.fairphone.spring.launcher.ui.modeicons._vector!!
        }
        com.fairphone.spring.launcher.ui.modeicons._vector = Builder(name = "Vector", defaultWidth = 24.0.dp, defaultHeight = 24.0.dp,
                viewportWidth = 24.0f, viewportHeight = 24.0f).apply {
            path(fill = SolidColor(Color(0xFFffffff)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(13.843f, 0.094f)
                curveTo(15.663f, 0.062f, 15.74f, 4.432f, 17.56f, 4.4f)
                curveTo(19.656f, 4.363f, 23.53f, 3.809f, 23.566f, 5.905f)
                curveTo(23.598f, 7.725f, 19.229f, 7.801f, 19.261f, 9.622f)
                curveTo(19.297f, 11.716f, 19.852f, 15.591f, 17.756f, 15.628f)
                curveTo(15.936f, 15.66f, 15.859f, 11.29f, 14.039f, 11.322f)
                curveTo(11.943f, 11.358f, 8.069f, 11.912f, 8.032f, 9.818f)
                curveTo(8.001f, 7.997f, 12.371f, 7.921f, 12.339f, 6.101f)
                curveTo(12.302f, 4.005f, 11.747f, 0.13f, 13.843f, 0.094f)
                close()
            }
        }
        .build()
        return com.fairphone.spring.launcher.ui.modeicons._vector!!
    }

private var _vector: ImageVector? = null

@Preview
@Composable
private fun Preview(): Unit {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = ModeIcons.Vector, contentDescription = "")
    }
}
