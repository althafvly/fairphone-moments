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

package com.fairphone.spring.launcher.util

import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.graphics.drawable.toBitmap
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.fairphone.spring.launcher.R
import com.fairphone.spring.launcher.data.model.colors
import com.fairphone.spring.launcher.data.model.protos.LauncherProfile
import com.fairphone.spring.launcher.ui.component.AnimatedBackground
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

const val LOCKSCREEN_PREFS_DATASTORE = "lockscreen_prefs"
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = LOCKSCREEN_PREFS_DATASTORE)

/**
 * Utility class for handling lockscreen wallpapers.
 */
object LockscreenUtils {

    private const val LOCKSCREEN_WALLPAPER_FILENAME = "lockscreen_wallpaper.png"

    private val LOCKSCREEN_WALLPAPER_TOP = intPreferencesKey("lockscreen_wallpaper_top")
    private val LOCKSCREEN_WALLPAPER_BOTTOM = intPreferencesKey("lockscreen_wallpaper_bottom")
    private val LOCKSCREEN_WALLPAPER_LEFT = intPreferencesKey("lockscreen_wallpaper_left")
    private val LOCKSCREEN_WALLPAPER_RIGHT = intPreferencesKey("lockscreen_wallpaper_right")

    suspend fun updateLockscreenWallpaper(
        context: Context,
        isDetoxEnabled: Boolean,
        activeProfile: LauncherProfile
    ) {
        if (isDetoxEnabled) {
            setDetoxLockscreenWallpaper(context, activeProfile)
        } else {
            restoreLockscreenWallpaper(context)
        }
    }

    /**
     * Saves the current lock screen wallpaper and sets a custom one.
     * Must be called from a background thread.
     */
    @SuppressLint("MissingPermission")
    suspend fun setDetoxLockscreenWallpaper(
        context: Context,
        activeProfile: LauncherProfile
    ): Result<Unit> {
        Log.d(Constants.LOG_TAG, "Saving current and setting new lock screen wallpaper.")
        val wallpaperManager = context.wallpaperManager()

        // 1. Save current lock screen wallpaper
        saveCurrentLockscreenWallpaper(context)

        // 2. Set new lock screen wallpaper
        //val detoxWallpaperFile = createLockscreenWallpaper(context, activeProfile)
        try {
            val newWallpaperResId = if (context.isDarkModeEnabled()) {
                R.drawable.bg_lockscreen_wallpaper_dark_essentials
            } else {
                R.drawable.bg_lockscreen_wallpaper_light_essentials
            }
            val drawable = AppCompatResources.getDrawable(context, newWallpaperResId)!!

            val bitmap = Bitmap.createBitmap(BitmapFactory.decodeResource(context.resources, newWallpaperResId))

            wallpaperManager.setBitmap(
                drawable.toBitmap(),
                Rect(),
                true,
                WallpaperManager.FLAG_LOCK,
            )
            Log.d(Constants.LOG_TAG, "Successfully set new lock screen wallpaper.")

            return Result.success(Unit)
        } catch (e: IOException) {
            Log.e(Constants.LOG_TAG, "IOException setting new lock screen wallpaper", e)
            return Result.failure(e)
        } catch (e: Resources.NotFoundException) {
            Log.e(Constants.LOG_TAG, "Error setting new wallpaper: Resource not found.", e)
            return Result.failure(e)
        } catch (e: Exception) {
            Log.e(Constants.LOG_TAG, "Error setting new lock screen wallpaper", e)
            return Result.failure(e)
        }
    }

    /**
     * Restores the previously saved lock screen wallpaper.
     * Must be called from a background thread.
     */
    @SuppressLint("MissingPermission")
    suspend fun restoreLockscreenWallpaper(context: Context): Result<Unit> {
        Log.d(Constants.LOG_TAG, "Restoring saved lock screen wallpaper.")
        val wallpaperManager = context.wallpaperManager()

        val savedWallpaperFile = File(context.filesDir, LOCKSCREEN_WALLPAPER_FILENAME)

        if (!savedWallpaperFile.exists()) {
            Log.w(Constants.LOG_TAG, "No saved wallpaper found to restore.")
            wallpaperManager.setBitmap(wallpaperManager.builtInDrawable.toBitmap())
            return Result.success(Unit)
        }

        try {
            val savedBitmap = BitmapFactory.decodeFile(savedWallpaperFile.absolutePath)
//            val savedDimensions = getWallpaperDimensions(context)
//
//            if (savedBitmap != null) {
//                wallpaperManager.setBitmap(
//                    savedBitmap,
//                    savedDimensions,
//                    true,
//                    WallpaperManager.FLAG_LOCK,
//                )
//                Log.d(Constants.LOG_TAG, "Successfully restored saved lock screen wallpaper.")
//                // Clean up the saved file after restoring
//                deleteSavedWallpaper(context)
//            } else {
//                Log.e(Constants.LOG_TAG, "Failed to decode saved wallpaper bitmap.")
//            }
            return Result.success(Unit)
        } catch (e: IOException) {
            Log.e(Constants.LOG_TAG, "IOException restoring saved lock screen wallpaper", e)
            return Result.failure(e)
        } catch (e: Exception) {
            Log.e(Constants.LOG_TAG, "Error restoring saved lock screen wallpaper", e)
            return Result.failure(e)
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun saveCurrentLockscreenWallpaper(
        context: Context,
    ) {
        val wallpaperManager = context.wallpaperManager()
        try {
            // Try getting the specific lock screen drawable
            val currentWallpaperDrawable: Drawable? =
                wallpaperManager.peekDrawable(WallpaperManager.FLAG_LOCK)
                    ?: wallpaperManager.getDrawable(WallpaperManager.FLAG_LOCK)
                    ?: wallpaperManager.getBuiltInDrawable(WallpaperManager.FLAG_LOCK)
            // TODO save bitmap dimensions

//            val currentWallpaperDimensions =
//                wallpaperManager.peekBitmapDimensions(WallpaperManager.FLAG_LOCK)
//
//            if (currentWallpaperDimensions != null) {
//                saveWallpaperDimensions(context, currentWallpaperDimensions)
//            }

            if (currentWallpaperDrawable != null) {
                val bitmapToSave = currentWallpaperDrawable.toBitmap()
                val saved = saveBitmapToFile(context, bitmapToSave, LOCKSCREEN_WALLPAPER_FILENAME)
                if (saved) {
                    Log.d(Constants.LOG_TAG, "Successfully saved lockscreen wallpaper.")
                } else {
                    Log.w(Constants.LOG_TAG, "Failed to save lockscreen wallpaper.")
                    // Decide if you should proceed without saving or abort
                }
            } else {
                Log.w(Constants.LOG_TAG, "Could not retrieve lockscreen wallpaper to save.")
                // Handle case where no specific lock screen wallpaper exists or couldn't be read
                // Maybe delete any previously saved file?
                deleteSavedWallpaper(context)
            }
        } catch (e: Exception) {
            Log.e(Constants.LOG_TAG, "Error saving current wallpaper", e)
            // Decide how to handle failure (e.g., don't set new one)
            return
        }
    }

    /**
     * Saves a bitmap to a file in the app's internal storage.
     * Returns true if successful, false otherwise.
     * Should be called from a background thread.
     */
    private suspend fun saveBitmapToFile(
        context: Context,
        bitmap: Bitmap,
        filename: String
    ): Boolean {
        return withContext(Dispatchers.IO) {
            val file = File(context.filesDir, filename)
            var fos: FileOutputStream? = null
            try {
                fos = FileOutputStream(file)
                // Use PNG for lossless compression; adjust format/quality as needed
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                true
            } catch (e: IOException) {
                Log.e(Constants.LOG_TAG, "Error saving bitmap to file $filename", e)
                false
            } finally {
                try {
                    fos?.close()
                } catch (e: IOException) {
                    // Ignore cleanup error
                }
            }
        }
    }

    private suspend fun saveWallpaperDimensions(context: Context, dimensions: Rect) {
        context.dataStore.edit { prefs ->
            prefs[LOCKSCREEN_WALLPAPER_TOP] = dimensions.top
            prefs[LOCKSCREEN_WALLPAPER_BOTTOM] = dimensions.bottom
            prefs[LOCKSCREEN_WALLPAPER_LEFT] = dimensions.left
            prefs[LOCKSCREEN_WALLPAPER_RIGHT] = dimensions.right
        }
    }

    private suspend fun getWallpaperDimensions(context: Context): Rect? {
        return context.dataStore.data.map { prefs ->
            val top = prefs[LOCKSCREEN_WALLPAPER_TOP]
            val bottom = prefs[LOCKSCREEN_WALLPAPER_BOTTOM]
            val left = prefs[LOCKSCREEN_WALLPAPER_LEFT]
            val right = prefs[LOCKSCREEN_WALLPAPER_RIGHT]

            if (top != null && bottom != null && left != null && right != null) {
                Rect(left, top, right, bottom)
            } else {
                null
            }

        }.first()
    }

    /**
     * Deletes the saved wallpaper file.
     */
    private fun deleteSavedWallpaper(context: Context): Boolean {
        val file = File(context.filesDir, LOCKSCREEN_WALLPAPER_FILENAME)
        if (file.exists()) {
            if (file.delete()) {
                Log.d(Constants.LOG_TAG, "Deleted saved wallpaper file.")
                return true
            } else {
                Log.w(Constants.LOG_TAG, "Failed to delete saved wallpaper file.")
                return false
            }
        }
        return true
    }

    suspend fun createLockscreenWallpaper(context: Context, activeProfile: LauncherProfile) {
        WallpaperUtils.captureComposableAsBitmap(
            context = context,
            targetWidthPx = 1116,
            targetHeightPx = 2484,
            composableContent = {
                AnimatedBackground(
                    colors = activeProfile.colors(),
                    content = { Box(modifier = Modifier.fillMaxSize()) }
                )
            }
        )?.let { bitmap ->
            WallpaperUtils.saveBitmapAsPng(
                context = context,
                bitmap = bitmap,
                fileName = "detox_lockscreen_wallpaper_${activeProfile.name}.png"
            )
        }?.let { file ->
            Log.d(Constants.LOG_TAG, "Saved lockscreen wallpaper to: ${file.absolutePath}")
        }
    }
}
