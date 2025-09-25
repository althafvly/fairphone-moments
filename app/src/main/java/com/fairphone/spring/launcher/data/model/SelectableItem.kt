/*
 * Copyright (C) 2025 FairPhone B.V.
 *
 * SPDX-FileCopyrightText: 2025. FairPhone B.V.
 *
 * SPDX-License-Identifier: EUPL-1.2
 */

package com.fairphone.spring.launcher.data.model

import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.vector.ImageVector
import coil3.compose.AsyncImage

/**
 * Interface for an item that can be selected in a list.
 */
sealed interface SelectableItem {
    /**
     * The unique identifier of the item.
     */
    val id: String

    /**
     * The name of the item.
     */
    val name: String

    /**
     * The icon of the item. Can be a [Drawable], [ImageVector] or any other type supported by [AsyncImage].
     */
    val icon: Any?
}
