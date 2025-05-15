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

package com.fairphone.spring.launcher.ui.icons

import androidx.compose.ui.graphics.vector.ImageVector
import com.fairphone.spring.launcher.ui.icons.mode.ModeIcon
import kotlin.collections.List as ____KtList

object NavIcons

private var __AllIcons: ____KtList<ImageVector>? = null

val NavIcons.AllIcons: ____KtList<ImageVector>
  get() {
    if (__AllIcons != null) {
      return __AllIcons!!
    }
    __AllIcons = ModeIcon.entries.map { it.imageVector }
    return __AllIcons!!
  }
