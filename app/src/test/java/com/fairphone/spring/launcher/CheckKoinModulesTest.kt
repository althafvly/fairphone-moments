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

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.fairphone.spring.launcher.analytics.AnalyticsService
import com.fairphone.spring.launcher.analytics.FirebaseAnalyticsService
import com.google.firebase.analytics.FirebaseAnalytics
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.check.checkModules
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner

@Suppress("DEPRECATION")
@RunWith(RobolectricTestRunner::class)
class CheckKoinModulesTest : KoinTest {

    private val context: Context = ApplicationProvider.getApplicationContext<Context>()
    lateinit var testModule: Module

    @Before
    fun setUp() {
        val mockFirebaseAnalytics = Mockito.mock(FirebaseAnalytics::class.java)
        val mockAnalyticsService = Mockito.mock(AnalyticsService::class.java)

        testModule = module {
            single { mockFirebaseAnalytics }
            single<AnalyticsService> { mockAnalyticsService }
            single<FirebaseAnalyticsService> { FirebaseAnalyticsService(get()) } // Pass mocked dependencies
        }
    }

    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun checkAllModules() {
        startKoin {
            androidContext(context)
            modules(App.koinModules + listOf(testModule))
        }.checkModules()
    }
}
