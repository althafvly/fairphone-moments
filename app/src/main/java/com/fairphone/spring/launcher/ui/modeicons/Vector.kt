/*
 * Copyright (c) 2025. Fairphone B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
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
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fairphone.spring.launcher.ui.ModeIcons

val ModeIcons.Vector: ImageVector
    get() {
        if (_vector != null) {
            return _vector!!
        }
        _vector = Builder(name = "Vector", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
            viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            group {
            }
            group {
                path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                    moveTo(9.621f, 15.6f)
                    curveTo(7.8f, 15.6f, 7.8f, 11.23f, 5.979f, 11.23f)
                    curveTo(3.883f, 11.23f, 0.0f, 11.716f, 0.0f, 9.62f)
                    curveTo(0.0f, 7.8f, 4.37f, 7.8f, 4.37f, 5.979f)
                    curveTo(4.37f, 3.884f, 3.883f, 0.0f, 5.979f, 0.0f)
                    curveTo(7.8f, 0.0f, 7.8f, 4.37f, 9.621f, 4.37f)
                    curveTo(11.717f, 4.37f, 15.6f, 3.884f, 15.6f, 5.979f)
                    curveTo(15.6f, 7.8f, 11.229f, 7.8f, 11.229f, 9.62f)
                    curveTo(11.229f, 11.716f, 11.717f, 15.6f, 9.621f, 15.6f)
                    close()
                }
            }
        }
            .build()
        return _vector!!
    }

private var _vector: ImageVector? = null

@Preview
@Composable
private fun Preview() {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = ModeIcons.Vector, contentDescription = "")
    }
}

val iconMap: Map<String, ImageVector> = mapOf(
    "Vector" to ModeIcons.Vector,
)


fun ImageVector.Companion.fromString(string: String): ImageVector {
    return iconMap.getOrElse(
        key = string,
        defaultValue = {
            ModeIcons.Vector
        }
    )
}