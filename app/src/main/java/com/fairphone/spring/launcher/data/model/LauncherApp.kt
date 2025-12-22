/*
 * Copyright (C) 2025 FairPhone B.V.
 *
 * SPDX-FileCopyrightText: 2025. FairPhone B.V.
 *
 * SPDX-License-Identifier: EUPL-1.2
 */

package com.fairphone.spring.launcher.data.model

import android.content.Context
import android.graphics.drawable.Drawable
import com.fairphone.spring.launcher.util.getDefaultBrowserPackageName
import com.fairphone.spring.launcher.util.getDefaultCameraPackageName
import com.fairphone.spring.launcher.util.getDefaultMessagingPackageName
import com.fairphone.spring.launcher.util.getDefaultNavigationPackageName
import com.fairphone.spring.launcher.util.getDefaultPhonePackageName

/**
 * Represents an application that can be launched. This is a sealed class,
 * meaning all possible subclasses are defined within this file.
 *
 * This class provides a common structure for different types of applications
 * that can be launched by the Spring Launcher.
 *
 * @property isWorkApp Indicates whether the application is a work profile app.
 */
sealed class LauncherApp(open val isWorkApp: Boolean) {
    /**
     * Represents the default browser application.
     *
     * @property isWorkApp Indicates whether is a work profile app. Defaults to `false`.
     * @property fallbackPackageName The package name to use as a fallback if the default browser cannot be determined.
     */
    data class Browser(
        override val isWorkApp: Boolean = false,
        val fallbackPackageName: String
    ) : LauncherApp(isWorkApp) {

        override fun getPackageName(context: Context): String =
            context.getDefaultBrowserPackageName(fallback = fallbackPackageName)
    }

    /**
     * Represents the default phone application.
     *
     * @property isWorkApp Indicates whether is a work profile app. Defaults to `false`.
     * @property fallbackPackageName The package name to use as a fallback if the default phone app cannot be determined.
     */
    data class Phone(
        override val isWorkApp: Boolean = false,
        val fallbackPackageName: String,
    ) : LauncherApp(isWorkApp) {

        override fun getPackageName(context: Context): String =
            context.getDefaultPhonePackageName(fallback = fallbackPackageName)
    }

    /**
     * Represents a messaging application launcher that handles the retrieval of
     * the package name for the default messaging app and provides a fallback package name.
     *
     * @property isWorkApp Indicates whether this application is part of a work profile.
     * @property fallbackPackageName The package name used as a fallback if the default messaging app cannot be determined.
     */
    data class Messages(
        override val isWorkApp: Boolean = false,
        val fallbackPackageName: String,
    ) : LauncherApp(isWorkApp) {

        override fun getPackageName(context: Context): String =
            context.getDefaultMessagingPackageName(fallback = fallbackPackageName)
    }

    /**
     * Represents a navigation application launcher that handles the retrieval of
     * the package name for the default navigation app and provides a fallback package name.
     *
     * @property isWorkApp Indicates whether this application is part of a work profile.
     * @property fallbackPackageName The package name used as a fallback if the default navigation app cannot be determined.
     */
    data class Navigation(
        override val isWorkApp: Boolean = false,
        val fallbackPackageName: String,
    ) : LauncherApp(isWorkApp) {

        override fun getPackageName(context: Context): String =
            context.getDefaultNavigationPackageName(fallback = fallbackPackageName)
    }

    /**
     * Represents a camera application that extends the functionalities of a launcher application.
     *
     * @property isWorkApp Indicates if this is a work profile application.
     * @property fallbackPackageName The fallback package name for the camera app if a default cannot be determined.
     */
    data class Camera(
        override val isWorkApp: Boolean = false,
        val fallbackPackageName: String,
    ) : LauncherApp(isWorkApp) {

        override fun getPackageName(context: Context): String =
            context.getDefaultCameraPackageName(fallback = fallbackPackageName)
    }

    /**
     * Represents a specific application identified by its package name.
     *
     * @property isWorkApp Indicates if this is a work profile application.
     * @property packageName The package name of the application.
     */
    data class App(
        override val isWorkApp: Boolean = false,
        val packageName: String
    ) : LauncherApp(isWorkApp) {
        override fun getPackageName(context: Context): String = packageName
    }

    /**
     * Abstract method to find the package name of the application.
     * @param context The application context.
     * @return The package name of the application, or `null` if it cannot be determined.
     */
    abstract fun getPackageName(context: Context): String

    /**
     * Launches the application.
     *
     * It retrieves the launch intent for the application's package and starts the activity.
     * If no launch intent is found, this method does nothing.
     *
     * @param context The application context.
     */
    fun launch(context: Context) {
        context.packageManager.getLaunchIntentForPackage(getPackageName(context))?.let { intent ->
            context.startActivity(intent)
        }
    }

    /**
     * Retrieves the icon of the application.
     *
     * @param context The application context.
     * @return The [Drawable] representing the application's icon, or `null` if it cannot be found.
     */
    fun icon(context: Context): Drawable? {
        return context.packageManager
            .getApplicationInfo(getPackageName(context), 0)
            .loadIcon(context.packageManager)
    }

    /**
     * Retrieves the name of the application.
     */
    fun appName(context: Context): String {
        return context.packageManager
            .getApplicationInfo(getPackageName(context), 0)
            .loadLabel(context.packageManager).toString()
    }
}

// Default apps
val Browser = LauncherApp.Browser(fallbackPackageName = "com.android.chrome")
val Camera = LauncherApp.Camera(fallbackPackageName = "com.fps.camera")
val Messages = LauncherApp.Messages(fallbackPackageName = "com.google.android.apps.messaging")
val Navigation = LauncherApp.Navigation(fallbackPackageName = "com.google.android.apps.maps")
val Phone = LauncherApp.Phone(fallbackPackageName = "com.google.android.dialer")


// Other apps
val Calm = LauncherApp.App(packageName = "com.calm.android")
val Clock = LauncherApp.App(packageName = "com.google.android.deskclock")
val Deezer = LauncherApp.App(packageName = "deezer.android.app")
val GoogleCalendar = LauncherApp.App(packageName = "com.google.android.calendar")
val GoogleDrive = LauncherApp.App(packageName = "com.google.android.apps.docs")
val GoogleGmail = LauncherApp.App(packageName = "com.google.android.gm")
val GoogleMeet = LauncherApp.App(packageName = "com.google.android.apps.tachyon")
val GoogleKeepNotes = LauncherApp.App(packageName = "com.google.android.keep")
val GooglePhoto = LauncherApp.App(packageName = "com.google.android.apps.photos")
val GoogleYoutubeMusic = LauncherApp.App(packageName = "com.google.android.apps.youtube.music")
val Headspace = LauncherApp.App(packageName = "com.getsomeheadspace.android")
val Spotify = LauncherApp.App(packageName = "com.spotify.music")
val Tidal = LauncherApp.App(packageName = "com.aspiro.tidal")
val Waze = LauncherApp.App(packageName = "com.waze")
val Whatsapp = LauncherApp.App(packageName = "com.whatsapp")

/**
 * A data class representing an app with a default [LauncherApp] and a list of alternative [LauncherApp]s.
 * This is used to define a category of app (e.g., "Music") where there's a primary app and other possible choices.
 *
 * @property default The primary or default [LauncherApp] for this app category.
 * @property alternatives A list of alternative [LauncherApp]s that can also fulfill the role of this app category. Defaults to an empty list.
 */
data class AppPreset(
    val default: LauncherApp,
    val alternatives: List<LauncherApp> = emptyList()
) {
    /** A combined list of the [default] application and all [alternatives]. */
    val allApps: List<LauncherApp> = listOf(default) + alternatives
}
