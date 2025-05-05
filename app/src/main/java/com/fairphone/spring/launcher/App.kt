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

package com.fairphone.spring.launcher

import android.app.Application
import android.content.Context
import androidx.annotation.VisibleForTesting
import com.fairphone.spring.launcher.data.AppPrefsImpl
import com.fairphone.spring.launcher.data.repository.LauncherProfileRepository
import com.fairphone.spring.launcher.di.dataModule
import com.fairphone.spring.launcher.di.uiModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application(), KoinComponent {

    companion object {
        @VisibleForTesting
        val koinModules = listOf(
            uiModule,
            dataModule,
        )
    }

    private val appPrefs: AppPrefsImpl by inject()
    private val LauncherProfileRepository: LauncherProfileRepository by inject()
    private lateinit var applicationScope: CoroutineScope

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            androidLogger(level = if (BuildConfig.DEBUG) Level.DEBUG else Level.INFO)
            modules(koinModules)
        }
        initApp(this)
    }

    private fun initApp(context: Context) {
        applicationScope = MainScope()
        applicationScope.launch {
            prepareInitialProfile(context)
        }
    }

    //TODO: This probably should be moved elsewhere
    private suspend fun prepareInitialProfile(context: Context) = withContext(Dispatchers.IO) {
        if (appPrefs.isFistTimeUse()) {
            LauncherProfileRepository.initialize(context)
            appPrefs.setFirstTimeUse(false)
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        applicationScope.cancel()
        applicationScope = MainScope()
    }
}
