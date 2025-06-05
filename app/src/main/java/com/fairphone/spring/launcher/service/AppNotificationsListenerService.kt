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

package com.fairphone.spring.launcher.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.IconCompat
import com.fairphone.spring.launcher.domain.usecase.profile.GetActiveProfileUseCase
import com.fairphone.spring.launcher.util.notificationManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

/**
 * Service to listen for notifications from specific apps and repost them.
 * This service requires the BIND_NOTIFICATION_LISTENER_SERVICE permission.
 */
class AppNotificationsListenerService() : NotificationListenerService() {

    companion object {
        const val TAG = "NotificationService"

        private const val MY_APP_NOTIFICATION_CHANNEL_ID = "InterceptedNotifications"
        private const val MY_APP_NOTIFICATION_CHANNEL_NAME = "Notifications"
    }

    // A coroutine scope tied to the service's lifecycle.
    // Using SupervisorJob so that if one coroutine fails, it doesn't cancel the whole scope.
    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private val getActiveProfileUseCase: GetActiveProfileUseCase by inject()

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    /**
     * This method is called when a notification is posted by an app.
     * @param sbn The StatusBarNotification object.
     */
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)
        Log.d(TAG, "Intercepted notification from: ${sbn.packageName}")

        try {

            // Discard notifications from launcher
            if (sbn.packageName == packageName) {
                return
            }

            serviceScope.launch {
                val activeProfile = getActiveProfileUseCase.execute(Unit).first()
                if (sbn.packageName in activeProfile.appNotificationsList) {
                    repostClonedNotification(this@AppNotificationsListenerService, sbn)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error while posting notification", e)
        }
    }

    /**
     * This method is called when a notification is removed from the status bar.
     * @param sbn The StatusBarNotification object.
     */
    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        Log.d(TAG, "Notification removed from target package: ${sbn.packageName}")
    }

    /**
     * Reposts a cloned version of the original notification.
     * This method ensures that the reposted notification retains as much of the original's properties as possible.
     * @param sbn The original StatusBarNotification to clone and repost.
     */
    @SuppressLint("RestrictedApi")
    private fun repostClonedNotification(context: Context, sbn: StatusBarNotification) {
        Log.d(TAG, "Showing intercepted notification: $sbn")
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        with (sbn.notification) {
            val builder = NotificationCompat.Builder(context, MY_APP_NOTIFICATION_CHANNEL_ID)
                .setExtras(extras)
                .setSmallIcon(IconCompat.createFromIcon(smallIcon))
                .setLargeIcon(getLargeIcon())
                .setWhen(`when`)
                .setShowWhen(true) // Ensure the timestamp is visible
                .setNumber(number)
                .setCategory(category)
                .setColor(color)
                .setSound(sound)
                .setVibrate(vibrate)
                .setGroup(group)
                .setContentIntent(contentIntent)
                .setDeleteIntent(deleteIntent)

            val isAutoCancel = (flags and Notification.FLAG_AUTO_CANCEL) != 0
            builder.setAutoCancel(isAutoCancel)

            actions?.forEach { action ->
                val compatAction = NotificationCompat.Action.Builder.fromAndroidAction(action).build()
                builder.addAction(compatAction)
            }

            notificationManager.notify(sbn.tag, sbn.id, builder.build())

            Log.d(TAG, "Successfully re-posted cloned notification.")
        }
    }

    /**
     * Creates a Notification Channel, which is required for all notifications on
     * Android 8.0 (API 26) and higher.
     */
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            MY_APP_NOTIFICATION_CHANNEL_ID,
            MY_APP_NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Channel for displaying notifications of allowed apps"
            setBypassDnd(true)
        }

        notificationManager().createNotificationChannel(channel)

    }
}
