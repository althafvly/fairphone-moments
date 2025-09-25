/*
 * Copyright (C) 2025 FairPhone B.V.
 *
 * SPDX-FileCopyrightText: 2025. FairPhone B.V.
 *
 * SPDX-License-Identifier: EUPL-1.2
 */

package com.fairphone.spring.launcher.domain.usecase.profile

import com.fairphone.spring.launcher.data.repository.LauncherProfileRepository
import com.fairphone.spring.launcher.domain.usecase.base.UseCase

class SetEditedProfileUseCase(
    private val launcherProfileRepository: LauncherProfileRepository,
) : UseCase<String, Unit>() {
    override suspend fun execute(params: String): Result<Unit> {
        launcherProfileRepository.setEditedProfile(params)
        return Result.success(Unit)
    }
}
