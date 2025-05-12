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

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.fairphone.spring.launcher.domain.usecase.profile.GetActiveProfileUseCase
import kotlinx.coroutines.flow.first
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Worker class for updating the lockscreen wallpaper.
 */
class LockscreenWallpaperSwitcherWorker(
    appContext: Context,
    params: WorkerParameters,
) : CoroutineWorker(appContext, params), KoinComponent {

    companion object {
        const val TAG = "LockscreenWallpaper"
        const val KEY_ENABLE = "key_enable"
        private const val WORK_NAME = "lockscreen_wallpaper_worker"

        /**
         * Creates input data and enqueues the WallpaperWorker using WorkManager.
         *
         * @param context The application context.
         * @param isDetoxEnabled wheter detox mode is enabled or not
         */
        fun enqueueWallpaperWork(context: Context, isDetoxEnabled: Boolean) {
            Log.d(TAG, "Enqueuing work with enable state: $isDetoxEnabled")

            // Create input data for the worker
            val inputData = workDataOf(KEY_ENABLE to isDetoxEnabled)

            // Create a OneTimeWorkRequest for the worker
            val wallpaperWorkRequest = OneTimeWorkRequestBuilder<LockscreenWallpaperSwitcherWorker>()
                .setInputData(inputData)
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .build()

            // Enqueue the work as unique work, replacing any existing pending work with the same name.
            // This ensures only the latest state change (after debouncing) is processed.
            WorkManager.getInstance(context).enqueueUniqueWork(
                WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                wallpaperWorkRequest
            )

            Log.d(TAG, "WallpaperWorker enqueued with unique name: $WORK_NAME")
        }
    }

    private val getProfileUseCase: GetActiveProfileUseCase by inject()

    override suspend fun doWork(): Result {
        Log.d(TAG, "Worker started.")
        val shouldEnable = inputData.getBoolean(KEY_ENABLE, false)
        Log.d(TAG, "Feature enable state from input data: $shouldEnable")

        try {
            val activeProfile = getProfileUseCase.execute(Unit).first()
            LockscreenUtils.updateLockscreenWallpaper(applicationContext, shouldEnable, activeProfile)

            return Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Error executing worker", e)
            return Result.failure()
        }
    }


}
