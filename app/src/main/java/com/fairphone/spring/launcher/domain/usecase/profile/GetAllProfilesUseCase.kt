/*
 * Copyright (C) 2025 FairPhone B.V.
 *
 * SPDX-FileCopyrightText: 2025. FairPhone B.V.
 *
 * SPDX-License-Identifier: EUPL-1.2
 */

package com.fairphone.spring.launcher.domain.usecase.profile

import com.fairphone.spring.launcher.data.model.protos.LauncherProfile
import com.fairphone.spring.launcher.data.repository.LauncherProfileRepository
import com.fairphone.spring.launcher.domain.usecase.base.FlowUseCase
import kotlinx.coroutines.flow.Flow

class GetAllProfilesUseCase(
    private val launcherProfileRepository: LauncherProfileRepository,
) : FlowUseCase<Unit, List<LauncherProfile>>() {
    override fun execute(params: Unit): Flow<List<LauncherProfile>> {
        return launcherProfileRepository.getProfiles()
    }
}
