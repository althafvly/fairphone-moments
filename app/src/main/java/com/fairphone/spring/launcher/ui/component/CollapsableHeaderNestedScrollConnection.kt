/*
 * Copyright (C) 2025 FairPhone B.V.
 *
 * SPDX-FileCopyrightText: 2025. FairPhone B.V.
 *
 * SPDX-License-Identifier: EUPL-1.2
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
