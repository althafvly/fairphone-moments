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

package com.fairphone.spring.launcher.ui.component

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import java.io.InputStream
import com.fairphone.spring.launcher.R

@Composable
fun AnimatedBackground(
    imageResIds: List<Int>,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val bitmaps = remember {
        imageResIds.map { resId ->
            val inputStream: InputStream = context.resources.openRawResource(resId)
            BitmapFactory.decodeStream(inputStream)
        }
    }

    var currentFrameIndex by remember { mutableIntStateOf(0) }

    val infiniteTransition = rememberInfiniteTransition(label = "infiniteTransition")
    val frameAnimation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = bitmaps.size.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000, // Adjust the duration for the animation speed
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Restart
        ), label = "frameAnimation"
    )

    currentFrameIndex = frameAnimation.toInt() % bitmaps.size

    LaunchedEffect(currentFrameIndex) {
        Log.d("AnimatedBackground", "currentFrameIndex: $currentFrameIndex")
    }

    Box(modifier = modifier.fillMaxSize()) {
        Image(
            bitmap = bitmaps[currentFrameIndex].asImageBitmap(),
            contentDescription = "Background Animation",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
    }
}
