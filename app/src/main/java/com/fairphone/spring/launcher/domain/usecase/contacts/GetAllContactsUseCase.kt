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