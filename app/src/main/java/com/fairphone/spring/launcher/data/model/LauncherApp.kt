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
     * @property isWorkApp Indicates whether the browser is a work profile app. Defaults to `false`.
     * @property packageNameResolver A lambda function that takes a [Context] and returns the package name of the default browser, or `null` if not found.
     */
    data class Browser(
        override val isWorkApp: Boolean = false,
        val packageNameResolver: ((Context) -> String),
    ) : LauncherApp(isWorkApp) {
        /**
         * Finds the package name of the default browser using the provided [packageNameResolver].
         * @param context The application context.
         * @return The package name of the default browser, or `null` if not found.
         */
        override fun getPackageName(context: Context): String =
            packageNameResolver.invoke(context)
    }

    /**
     * Represents a specific application identified by its package name.
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

val DefaultBrowser = LauncherApp.Browser(packageNameResolver = { context -> getDefaultBrowserPackageName(context) })
val Calm = LauncherApp.App(packageName = "com.calm.android")
val Camera = LauncherApp.App(packageName = "com.fps.camera")
val Clock = LauncherApp.App(packageName = "com.google.android.deskclock")
val Deezer = LauncherApp.App(packageName = "deezer.android.app")
val GoogleCalendar = LauncherApp.App(packageName = "com.google.android.calendar")
val GoogleDrive = LauncherApp.App(packageName = "com.google.android.apps.docs")
val GoogleGmail = LauncherApp.App(packageName = "com.google.android.gm")
val GoogleMaps = LauncherApp.App(packageName = "com.google.android.apps.maps")
val GoogleMeet = LauncherApp.App(packageName = "com.google.android.apps.tachyon")
val GoogleKeepNotes = LauncherApp.App(packageName = "com.google.android.keep")
val GooglePhoto = LauncherApp.App(packageName = "com.google.android.apps.photos")
val GoogleYoutubeMusic = LauncherApp.App(packageName = "com.google.android.apps.youtube.music")
val Headspace = LauncherApp.App(packageName = "com.getsomeheadspace.android")
val Messages = LauncherApp.App(packageName = "com.google.android.apps.messaging")
val Phone = LauncherApp.App(packageName = "com.google.android.dialer")
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
