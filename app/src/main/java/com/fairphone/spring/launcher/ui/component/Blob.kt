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

package com.fairphone.spring.launcher.ui.component

import android.graphics.Rect
import android.graphics.RectF
import android.text.TextUtils
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.graphics.PathParser

/**
 * Shape for the background of the home screen
 */
class Blob : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val pathData =
            "M248.33,23.3C318.6,82.5 348.2,194.14 391.32,422.72C392.27,435.59 392.2,448.48 392.19,461.38C392.19,462.59 392.19,463.81 392.19,465.07C392.01,599.42 392.01,599.42 350.07,643.99C314.21,678.77 259.64,676.54 213.19,676.32C209.27,676.31 205.34,676.31 201.42,676.32C158.88,676.36 115.03,676.01 76,657C75.02,656.53 74.04,656.06 73.03,655.57C41.25,639.34 23.66,608.8 12.96,576.07C6.11,554.12 2.68,530.89 1,508C0.91,506.87 0.82,505.74 0.73,504.58C-0.21,490.94 -0.2,477.3 -0.19,463.63C-0.19,462.36 -0.19,461.1 -0.19,459.79C-0.16,439.46 0.27,419.26 2,399C2.06,398.25 2.13,397.49 2.19,396.71C9.58,308.84 26.97,219.96 120,39.15C121.84,37.18 123.54,35.14 125.25,33.06C159.37,-5.75 208.93,-9.12 248.33,23.3Z"
        val xScale = size.width / 392f
        val yScale = size.height / 676f
        val path = PathParser.createPathFromPathData(pathData)
        val scaleMatrix = android.graphics.Matrix()
        scaleMatrix.setScale(xScale, yScale)
        path.transform(scaleMatrix)


        return Outline.Generic(path.asComposePath())
    }
}

@Composable
@Preview
fun Blob_Preview() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Surface(
            shape = Blob(),
            color = Color(0xB2C3D1D0),
            modifier = Modifier
                .width(200.dp)
                .height(350.dp)
                .offset { IntOffset(-10, 0) }
        ) {}
    }
}

/**
 * Alternative implementation of the background shape of the home screen
 */
@Suppress("DEPRECATION")
class ShapePath(private val pathData: String) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(path = drawPath(size))
    }

    private fun drawPath(size: Size): Path {
        return Path().apply {
            reset()
            if (!TextUtils.isEmpty(pathData)) {
                val scaleMatrix = android.graphics.Matrix()
                val rectF = RectF()
                val path = PathParser.createPathFromPathData(pathData).asComposePath()
                val rectPath = with(path.getBounds()) {
                    Rect(
                        left.toInt(),
                        top.toInt(),
                        right.toInt(),
                        bottom.toInt()
                    )
                }
                val scaleXFactor = size.width / rectPath.width().toFloat()
                val scaleYFactor = size.height / rectPath.height().toFloat()
                val androidPath = path.asAndroidPath()
                scaleMatrix.setScale(scaleXFactor, scaleYFactor, rectF.centerX(), rectF.centerY())
                androidPath.computeBounds(rectF, true)
                androidPath.transform(scaleMatrix)
                return androidPath.asComposePath()
            }
            close()
        }
    }
}



const val EGG_SHAPE_PATH =
    "M248.33,23.3C318.6,82.5 348.2,194.14 391.32,422.72C392.27,435.59 392.2,448.48 392.19,461.38C392.19,462.59 392.19,463.81 392.19,465.07C392.01,599.42 392.01,599.42 350.07,643.99C314.21,678.77 259.64,676.54 213.19,676.32C209.27,676.31 205.34,676.31 201.42,676.32C158.88,676.36 115.03,676.01 76,657C75.02,656.53 74.04,656.06 73.03,655.57C41.25,639.34 23.66,608.8 12.96,576.07C6.11,554.12 2.68,530.89 1,508C0.91,506.87 0.82,505.74 0.73,504.58C-0.21,490.94 -0.2,477.3 -0.19,463.63C-0.19,462.36 -0.19,461.1 -0.19,459.79C-0.16,439.46 0.27,419.26 2,399C2.06,398.25 2.13,397.49 2.19,396.71C9.58,308.84 26.97,219.96 120,39.15C121.84,37.18 123.54,35.14 125.25,33.06C159.37,-5.75 208.93,-9.12 248.33,23.3Z"


@Composable
@Preview
fun ShapePath_Preview() {
    val shapePath = remember { ShapePath(EGG_SHAPE_PATH) }
    Surface(
        shape = shapePath,
        color = Color(0xB2C3D1D0),
        modifier = Modifier
            .width(500.dp)
            .height(676.dp)
        //.graphicsLayer { rotationZ = 45f }
    ) { }
}
