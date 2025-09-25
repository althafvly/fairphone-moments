/*
 * Copyright (C) 2025 FairPhone B.V.
 *
 * SPDX-FileCopyrightText: 2025. FairPhone B.V.
 *
 * SPDX-License-Identifier: EUPL-1.2
 */

package com.fairphone.spring.launcher.domain.usecase.base

import kotlinx.coroutines.flow.Flow

/**
 * Base class for Use Cases
 */
abstract class FlowUseCase<INPUT, RESULT> {
    abstract fun execute(params: INPUT): Flow<RESULT>
}
