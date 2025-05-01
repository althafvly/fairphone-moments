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
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.fairphone.spring.launcher.data.datasource.ProfileDataSource
import com.fairphone.spring.launcher.data.datasource.ProfileDataSourceImpl
import com.fairphone.spring.launcher.data.model.LauncherProfile
import com.fairphone.spring.launcher.data.prefs.AppPrefs
import com.fairphone.spring.launcher.data.prefs.AppPrefsImpl
import com.fairphone.spring.launcher.data.repository.AppInfoRepository
import com.fairphone.spring.launcher.data.repository.AppInfoRepositoryImpl
import com.fairphone.spring.launcher.data.repository.LauncherProfileRepository
import com.fairphone.spring.launcher.data.repository.LauncherProfileRepositoryImpl
import com.fairphone.spring.launcher.data.serializer.LauncherProfileSerializer
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataModule = module {
    singleOf(::AppInfoRepositoryImpl) { bind<AppInfoRepository>() }
    singleOf(::LauncherProfileRepositoryImpl) { bind<LauncherProfileRepository>() }
    single<ProfileDataSource>{ ProfileDataSourceImpl(androidContext().profileDataStore) }
    single<AppPrefs>{ AppPrefsImpl(androidContext().appPrefsDataStore) }
}

/**
 * DataStore used to store App Prefs
 */
val Context.appPrefsDataStore: DataStore<Preferences> by preferencesDataStore(name = "app_prefs")

/**
 * DataStore used to store LauncherProfiles
 */
val Context.profileDataStore: DataStore<LauncherProfile> by dataStore(
    fileName = "profile.pb",
    serializer = LauncherProfileSerializer
)

