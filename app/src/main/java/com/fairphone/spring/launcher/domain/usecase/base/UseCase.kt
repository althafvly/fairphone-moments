/*
 * Copyright (C) 2025 FairPhone B.V.
 *
 * SPDX-FileCopyrightText: 2025. FairPhone B.V.
 *
 * SPDX-License-Identifier: EUPL-1.2
 */

package com.fairphone.spring.launcher.domain.usecase.base

/**
 * Base class for Use Cases
 */
abstract class UseCase<INPUT, RESULT> {
    abstract suspend fun execute(params: INPUT): Result<RESULT>
}