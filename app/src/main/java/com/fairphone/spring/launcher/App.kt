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
import android.util.Log
import androidx.annotation.VisibleForTesting
import com.fairphone.spring.launcher.data.model.CreateLauncherProfile
import com.fairphone.spring.launcher.data.model.Defaults
import com.fairphone.spring.launcher.data.model.Defaults.DEFAULT_VISIBLE_APPS
import com.fairphone.spring.launcher.data.prefs.AppPrefs
import com.fairphone.spring.launcher.di.dataModule
import com.fairphone.spring.launcher.di.domainModule
import com.fairphone.spring.launcher.di.uiModule
import com.fairphone.spring.launcher.domain.usecase.profile.CreateLauncherProfileUseCase
import com.fairphone.spring.launcher.util.getDefaultBrowserPackageName
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
            domainModule,
        )
    }

    private val appPrefs: AppPrefs by inject()
    private val createLauncherProfileUseCase: CreateLauncherProfileUseCase by inject()
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

    //TODO: This should be moved elsewhere
    private suspend fun prepareInitialProfile(context: Context) = withContext(Dispatchers.IO) {
        if (appPrefs.isFistTimeUse()) {

            createInitialPresets(context)

            appPrefs.setFirstTimeUse(false)
        }
    }

    private suspend fun createInitialPresets(context: Context) {
        val defaultBrowser = getDefaultBrowserPackageName(context)
        val defaultVisibleApps = DEFAULT_VISIBLE_APPS + defaultBrowser
        val essentials = CreateLauncherProfile(
            name = context.getString(R.string.default_profile_name),
            icon = Defaults.DEFAULT_ICON,
            bgColor1 = Defaults.DEFAULT_BG_COLOR1,
            bgColor2 = Defaults.Color_BG_Orange,
            visibleApps = defaultVisibleApps,
            allowedContacts = Defaults.DEFAULT_ALLOWED_CONTACTS,
            repeatCallEnabled = Defaults.DEFAULT_REPEAT_CALL_ENABLED,
            wallpaperId = Defaults.DEFAULT_WALLPAPER_ID,
            uiMode = Defaults.DEFAULT_DARK_MODE_SETTING,
            blueLightFilterEnabled = Defaults.DEFAULT_BLUE_LIGHT_FILTER_ENABLED,
            soundSetting = Defaults.DEFAULT_SOUND_SETTING,
            batterySaverEnabled = Defaults.BATTERY_SAVER_ENABLED,
            reduceBrightnessEnabled = Defaults.REDUCE_BRIGHTNESS_ENABLED,
        )
        val result = createLauncherProfileUseCase.execute(essentials)

        // TODO remove this code (necessary to have 2 default prfiles)
        val balance = CreateLauncherProfile(
            name = "Balance",
            icon = Defaults.DEFAULT_ICON,
            bgColor1 = Defaults.DEFAULT_BG_COLOR1,
            bgColor2 = Defaults.Color_BG_Blue,
            visibleApps = defaultVisibleApps,
            allowedContacts = Defaults.DEFAULT_ALLOWED_CONTACTS,
            repeatCallEnabled = Defaults.DEFAULT_REPEAT_CALL_ENABLED,
            wallpaperId = Defaults.DEFAULT_WALLPAPER_ID,
            uiMode = Defaults.DEFAULT_DARK_MODE_SETTING,
            blueLightFilterEnabled = Defaults.DEFAULT_BLUE_LIGHT_FILTER_ENABLED,
            soundSetting = Defaults.DEFAULT_SOUND_SETTING,
            batterySaverEnabled = Defaults.BATTERY_SAVER_ENABLED,
            reduceBrightnessEnabled = Defaults.REDUCE_BRIGHTNESS_ENABLED,
        )

        val spring = CreateLauncherProfile(
            name = "Spring",
            icon = Defaults.DEFAULT_ICON,
            bgColor1 = Defaults.DEFAULT_BG_COLOR1,
            bgColor2 = Defaults.Color_BG_Green,
            visibleApps = defaultVisibleApps,
            allowedContacts = Defaults.DEFAULT_ALLOWED_CONTACTS,
            repeatCallEnabled = Defaults.DEFAULT_REPEAT_CALL_ENABLED,
            wallpaperId = Defaults.DEFAULT_WALLPAPER_ID,
            uiMode = Defaults.DEFAULT_DARK_MODE_SETTING,
            blueLightFilterEnabled = Defaults.DEFAULT_BLUE_LIGHT_FILTER_ENABLED,
            soundSetting = Defaults.DEFAULT_SOUND_SETTING,
            batterySaverEnabled = Defaults.BATTERY_SAVER_ENABLED,
            reduceBrightnessEnabled = Defaults.REDUCE_BRIGHTNESS_ENABLED,
        )


        createLauncherProfileUseCase.execute(balance)
        createLauncherProfileUseCase.execute(spring)

        if (result.isFailure) {
            Log.e("App", "Failed to create initial profile", result.exceptionOrNull())
        }

    }

    override fun onLowMemory() {
        super.onLowMemory()
        applicationScope.cancel()
        applicationScope = MainScope()
    }
}
