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
