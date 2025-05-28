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

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource

@Stable
class CollapsableHeaderNestedScrollConnection(val topBarHeight: Int) : NestedScrollConnection {

    var topBarOffset: Int by mutableIntStateOf(0)
        private set

    override fun onPreScroll(
        available: Offset,
        source: NestedScrollSource
    ): Offset {
        val delta = available.y.toInt()
        val newOffset = topBarOffset + delta
        val previousOffset = topBarOffset
        topBarOffset = newOffset.coerceIn(-topBarHeight, 0)
        val consumed = topBarOffset - previousOffset
        return Offset(0f, consumed.toFloat())
    }
}
