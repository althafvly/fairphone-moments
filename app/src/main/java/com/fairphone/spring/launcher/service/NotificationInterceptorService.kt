/*
 * Copyright (C) 2025 FairPhone B.V.
 *
 * SPDX-FileCopyrightText: 2025. FairPhone B.V.
 *
 * SPDX-License-Identifier: EUPL-1.2
 */

package com.fairphone.spring.launcher.service

import android.app.Notification
import android.app.Person
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.core.net.toUri
import com.fairphone.spring.launcher.data.model.protos.ContactType
import com.fairphone.spring.launcher.data.model.protos.LauncherProfile
import com.fairphone.spring.launcher.domain.usecase.profile.GetActiveProfileUseCase
import com.fairphone.spring.launcher.util.isAppDefaultLauncher
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
class NotificationInterceptorService() : NotificationListenerService() {

    companion object {
        const val TAG = "NotificationService"
//
//        private const val MY_APP_NOTIFICATION_CHANNEL_ID = "InterceptedNotifications"
//        private const val MY_APP_NOTIFICATION_CHANNEL_NAME = "Notifications"
    }

    private lateinit var serviceScope: CoroutineScope

    private val getActiveProfileUseCase: GetActiveProfileUseCase by inject()

    override fun onCreate() {
        super.onCreate()
        serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
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
        if (!isAppDefaultLauncher()) {
            Log.d(TAG, "Not the default launcher. Ignoring onNotificationPosted()")
            return
        }

        Log.d(
            TAG, "Intercepted notification from: ${sbn.packageName} " +
                    "id: ${sbn.id}, tag: ${sbn.tag}, " +
                    "extras: ${sbn.notification.extras.size()}"
        )

        serviceScope.launch {
            if (!shouldDisplayNotification(sbn)) {
                cancelNotification(sbn.key)
            }
        }
    }

    private suspend fun shouldDisplayNotification(sbn: StatusBarNotification): Boolean {
        val activeProfile = getActiveProfileUseCase.execute(Unit).first()

        if (isAppAllowed(activeProfile, sbn)) {
            Log.d(TAG, "App is allowed")
            return true
        }

        if (isFromAllowedContact(activeProfile, sbn)) {
            Log.d(TAG, "Contact is allowed")
            return true
        }

        return false
    }

    private fun isAppAllowed(currentProfile: LauncherProfile, sbn: StatusBarNotification): Boolean {
        return sbn.packageName in currentProfile.appNotificationsList
    }

    private fun isFromAllowedContact(
        currentProfile: LauncherProfile,
        sbn: StatusBarNotification
    ): Boolean {
        val extras = sbn.notification.extras

        val notificationPersons = mutableListOf<Person>()

        notificationPersons.addAll(
            extras.getParcelableArrayList(
                Notification.EXTRA_PEOPLE_LIST,
                Person::class.java
            )?.toList() ?: emptyList()
        )

        extras.getParcelable(Notification.EXTRA_CALL_PERSON, Person::class.java)?.let {
            notificationPersons.add(it)
        }
        extras.getParcelable(Notification.EXTRA_MESSAGING_PERSON, Person::class.java)?.let {
            notificationPersons.add(it)
        }

        notificationPersons.forEach { notificationPerson ->
            Log.d(
                TAG, "Notification Person: uri: ${notificationPerson.uri?.clearQuery()}, " +
                        "name: ${notificationPerson.name}, " +
                        "key: ${notificationPerson.key}, " +
                        "isBot: ${notificationPerson.isBot}, " +
                        "isImportant: ${notificationPerson.isImportant}, " +
                        "icon: ${notificationPerson.icon}"
            )
        }

        if (notificationPersons.isEmpty()) {
            Log.d(TAG, "No Person found in extras")
            return false
        }

        when (currentProfile.allowedContacts) {
            ContactType.CONTACT_TYPE_CUSTOM -> {
                val personsUris = notificationPersons.mapNotNull { it.uri?.clearQuery() }
                return personsUris.any { it in currentProfile.customContactsList }.also {
                    Log.d(TAG, "Person is in custom contacts list: $it")
                }
            }

            ContactType.CONTACT_TYPE_NONE -> return false
            else -> return true  // All contacts should go through via Zen Policy
        }
    }

    /**
     * This method is called when a notification is removed from the status bar.
     * @param sbn The StatusBarNotification object.
     */
    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        Log.d(TAG, "Notification removed from target package: ${sbn.packageName}")
        super.onNotificationRemoved(sbn)
        cancelNotification(sbn.key)
        notificationManager().cancel(sbn.tag, sbn.id)
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        Log.i(TAG, "Notification Listener connected.")
    }

    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
        Log.w(TAG, "Notification Listener disconnected.")
    }

//    /**
//     * Reposts a cloned version of the original notification.
//     * This method ensures that the reposted notification retains as much of the original's properties as possible.
//     * @param sbn The original StatusBarNotification to clone and repost.
//     */
//    @Suppress("DEPRECATION")
//    @SuppressLint("RestrictedApi")
//    private fun repostClonedNotification(context: Context, sbn: StatusBarNotification) {
//        Log.d(TAG, "Re-posting intercepted notification: $sbn")
//        val originalNotification = sbn.notification
//
//        val builder = NotificationCompat.Builder(this, MY_APP_NOTIFICATION_CHANNEL_ID)
//            .setExtras(originalNotification.extras)
//            .setSmallIcon(IconCompat.createFromIcon(originalNotification.smallIcon))
//            .setLargeIcon(originalNotification.getLargeIcon())
//            .setWhen(originalNotification.`when`)
//            .setShowWhen(true)
//            .setNumber(originalNotification.number)
//            .setCategory(originalNotification.category)
//            .setColor(originalNotification.color)
//            .setSound(originalNotification.sound)
//            .setVibrate(originalNotification.vibrate)
//            .setGroup(originalNotification.group)
//            .setAutoCancel((originalNotification.flags and Notification.FLAG_AUTO_CANCEL) != 0)
//            .setContentIntent(originalNotification.contentIntent)
//            .setDeleteIntent(originalNotification.deleteIntent)
//
//        originalNotification.actions?.forEach { action ->
//            val compatAction = NotificationCompat.Action.Builder.fromAndroidAction(action).build()
//            builder.addAction(compatAction)
//        }
//
//        when (originalNotification.category) {
//            Notification.CATEGORY_CALL -> {
//                builder.setFullScreenIntent(originalNotification.fullScreenIntent, true)
//                startForeground(
//                    sbn.id,
//                    builder.build(),
//                    ServiceInfo.FOREGROUND_SERVICE_TYPE_PHONE_CALL
//                )
//            }
//
//            else -> {
//                context.notificationManager().notify(sbn.tag, sbn.id, builder.build())
//            }
//        }
//
//
//        Log.d(TAG, "Successfully re-posted cloned notification.")
//    }

//    /**
//     * Creates a Notification Channel, which is required for all notifications on
//     * Android 8.0 (API 26) and higher.
//     */
//    private fun createNotificationChannel() {
//        val channel = NotificationChannel(
//            MY_APP_NOTIFICATION_CHANNEL_ID,
//            MY_APP_NOTIFICATION_CHANNEL_NAME,
//            NotificationManager.IMPORTANCE_DEFAULT
//        ).apply {
//            description = "Channel for displaying notifications of allowed apps"
//            setBypassDnd(true)
//        }
//
//        notificationManager().createNotificationChannel(channel)
//
//    }
}

fun String?.clearQuery(): String = this?.toUri()?.buildUpon()?.clearQuery()?.build().toString()