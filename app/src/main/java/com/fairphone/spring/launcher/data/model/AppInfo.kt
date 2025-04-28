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

data class AppInfo(
    val name: String,
    val packageName: String,
    val mainActivityClassName: String,
    val userUuid: Int = 0,
    val icon: Drawable,
) {
    override fun equals(other: Any?): Boolean {
        return other is AppInfo && other.packageName == packageName
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
