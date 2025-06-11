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
import com.fairphone.spring.launcher.ui.component.selector.SelectableItem

data class AppInfo(
    override val name: String,
    val packageName: String,
    val mainActivityClassName: String,
    val userUuid: Int = 0,
    override val icon: Drawable,
    val isWorkApp: Boolean = false
) : SelectableItem {

    override val id: String = packageName

    override fun equals(other: Any?): Boolean {
        return other is AppInfo &&
                other.packageName == packageName &&
                other.isWorkApp == isWorkApp &&
                other.userUuid == userUuid
    }

    override fun hashCode(): Int {
        var result = userUuid
        result = 31 * result + name.hashCode()
        result = 31 * result + packageName.hashCode()
        result = 31 * result + mainActivityClassName.hashCode()
        result = 31 * result + icon.hashCode()
        return result
    }
}

const val LAUNCHER_MAX_APP_COUNT = 5
