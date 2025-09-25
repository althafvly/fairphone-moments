/*
 * Copyright (C) 2025 FairPhone B.V.
 *
 * SPDX-FileCopyrightText: 2025. FairPhone B.V.
 *
 * SPDX-License-Identifier: EUPL-1.2
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
