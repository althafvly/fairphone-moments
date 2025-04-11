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

package com.fairphone.spring.launcher.di

import android.content.Context
import com.fairphone.spring.launcher.data.repository.AppInfoRepository
import com.fairphone.spring.launcher.data.repository.IAppInfoRepository
import com.fairphone.spring.launcher.data.repository.IMomentRepository
import com.fairphone.spring.launcher.data.repository.MomentRepository
import com.fairphone.spring.launcher.data.repository.momentDataStore
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val dataModule = module {
    factoryOf(::AppInfoRepository) { bind<IAppInfoRepository>() }
    factoryOf(::MomentRepository) { bind<IMomentRepository>() }
    single { get<Context>().momentDataStore }
}
