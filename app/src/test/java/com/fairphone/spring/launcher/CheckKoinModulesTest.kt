/*
 * Copyright (C) 2025 FairPhone B.V.
 *
 * SPDX-FileCopyrightText: 2025. FairPhone B.V.
 *
 * SPDX-License-Identifier: EUPL-1.2
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
