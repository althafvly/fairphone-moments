/*
 * Copyright (C) 2025 FairPhone B.V.
 *
 * SPDX-FileCopyrightText: 2025. FairPhone B.V.
 *
 * SPDX-License-Identifier: EUPL-1.2
 */

package com.fairphone.spring.launcher.domain.usecase.contacts

import com.fairphone.spring.launcher.data.datasource.DeviceContactDataSource
import com.fairphone.spring.launcher.data.model.ContactInfo
import com.fairphone.spring.launcher.domain.usecase.base.UseCase

class GetAllContactsUseCase(
    private val deviceContactDataSource: DeviceContactDataSource,
) : UseCase<Unit, List<ContactInfo>>() {
    override suspend fun execute(params: Unit): Result<List<ContactInfo>> {
        return Result.success(deviceContactDataSource.getDeviceContacts())
    }
}