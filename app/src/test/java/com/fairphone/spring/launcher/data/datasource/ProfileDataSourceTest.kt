/*
 * Copyright (C) 2025 FairPhone B.V.
 *
 * SPDX-FileCopyrightText: 2025. FairPhone B.V.
 *
 * SPDX-License-Identifier: EUPL-1.2
 */

package com.fairphone.spring.launcher.data.datasource

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.test.core.app.ApplicationProvider
import com.fairphone.spring.launcher.data.model.Defaults
import com.fairphone.spring.launcher.data.model.LauncherColors
import com.fairphone.spring.launcher.data.model.protos.LauncherProfile
import com.fairphone.spring.launcher.data.model.protos.LauncherProfiles
import com.fairphone.spring.launcher.data.model.protos.launcherProfile
import com.fairphone.spring.launcher.data.model.protos.launcherProfileApp
import com.fairphone.spring.launcher.di.profileDataStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class ProfileDataSourceTest {

    private val context: Context = ApplicationProvider.getApplicationContext()
    private lateinit var dataStore: DataStore<LauncherProfiles>
    private lateinit var profileDataSource: ProfileDataSourceImpl

    @Before
    fun setUp() {
        dataStore = context.profileDataStore
        profileDataSource = ProfileDataSourceImpl(dataStore)
    }

    @After
    fun tearDown() = runTest {
        dataStore.updateData { LauncherProfiles.getDefaultInstance() }
    }

    @Test
    fun `getActive returns active profile`() = runTest {
        val testProfile = createTestProfile("test_profile", context)
        profileDataSource.createLauncherProfile(testProfile)
        profileDataSource.setActiveProfile(testProfile.id)

        val activeProfile = profileDataSource.getActiveProfile().first()
        assert(activeProfile.id == testProfile.id)
    }

    @Test
    fun `getActive returns first profile if no active is set`() = runTest {
        val testProfile1 = createTestProfile("test_profile1", context)
        val testProfile2 = createTestProfile("test_profile2", context)
        val testProfile3 = createTestProfile("test_profile3", context)
        profileDataSource.createLauncherProfile(testProfile1)
        profileDataSource.createLauncherProfile(testProfile2)
        profileDataSource.createLauncherProfile(testProfile3)


        val activeProfile = profileDataSource.getActiveProfile().first()
        assert(activeProfile.id == testProfile1.id)
    }
}

fun createTestProfile(profileId: String, context: Context): LauncherProfile = launcherProfile {
    id = profileId
    name = "Test Profile"
    icon = Defaults.DEFAULT_ICON
    bgColor1 = LauncherColors.Default.leftColor
    bgColor2 = LauncherColors.Default.rightColor
    launcherProfileApps.addAll(
        Defaults.DEFAULT_VISIBLE_APPS.map { app ->
            app.allApps.firstNotNullOf {
                launcherProfileApp {
                    packageName = it.getPackageName(context)
                    isWorkApp = it.isWorkApp
                }
            }
        }
    )
    allowedContacts = Defaults.DEFAULT_ALLOWED_CONTACTS
    repeatCallEnabled = Defaults.DEFAULT_REPEAT_CALL_ENABLED
    wallpaperId = Defaults.DEFAULT_WALLPAPER_ID
    uiMode = Defaults.DEFAULT_DARK_MODE_SETTING
    blueLightFilterEnabled = Defaults.DEFAULT_BLUE_LIGHT_FILTER_ENABLED
    soundSetting = Defaults.DEFAULT_SOUND_SETTING
    batterySaverEnabled = Defaults.BATTERY_SAVER_ENABLED
    reduceBrightnessEnabled = Defaults.REDUCE_BRIGHTNESS_ENABLED
}
