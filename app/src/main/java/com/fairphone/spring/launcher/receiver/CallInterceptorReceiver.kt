/*
 * Copyright (C) 2025 FairPhone B.V.
 *
 * SPDX-FileCopyrightText: 2025. FairPhone B.V.
 *
 * SPDX-License-Identifier: EUPL-1.2
 */

package com.fairphone.spring.launcher.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.telephony.TelephonyManager
import android.util.Log
import com.fairphone.spring.launcher.data.datasource.DeviceContactDataSource
import com.fairphone.spring.launcher.domain.usecase.profile.GetActiveProfileUseCase
import com.fairphone.spring.launcher.util.RingerModeManager
import com.fairphone.spring.launcher.util.isAppDefaultLauncher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * A BroadcastReceiver that listens for phone state changes to intercept incoming calls.
 * This version silences calls from contacts NOT on the allow list.
 */
class CallInterceptorReceiver : BroadcastReceiver(), KoinComponent {

    companion object {
        private const val TAG = "CallInterceptor"
    }

    // A coroutine scope tied to the service's lifecycle.
    // Using SupervisorJob so that if one coroutine fails, it doesn't cancel the whole scope.
    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private val getActiveProfileUseCase: GetActiveProfileUseCase by inject()
    private val contactDataSource: DeviceContactDataSource by inject()

    override fun onReceive(context: Context, intent: Intent?) {
        if (!context.isAppDefaultLauncher()) {
            Log.d(TAG, "Not the default launcher. Ignoring call broadcast.")
            return
        }
        // Ensure the intent action is for phone state changes
        if (intent?.action != TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            return
        }

        val stateStr = intent.extras?.getString(TelephonyManager.EXTRA_STATE)
        if (stateStr == null) {
            Log.w(TAG, "Did not receive a state string from the intent.")
            return
        }

        // Get the AudioManager to control the ringer
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        serviceScope.launch {

            when (stateStr) {
                TelephonyManager.EXTRA_STATE_RINGING -> {
                    // This is the state when the phone is ringing.
                    val incomingNumber =
                        intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

                    if (incomingNumber.isNullOrEmpty()) {
                        Log.d(TAG, "Incoming number is null or empty. Cannot filter.")
                        return@launch
                    }

                    Log.d(TAG, "Incoming call from: $incomingNumber")

                    // ** UPDATED LOGIC **
                    // If the contact is NOT on the allow list, silence the ringer.
                    // Otherwise, the call will proceed normally.
                    if (!isNumberOnAllowList(context, incomingNumber)) {
                        Log.i(
                            TAG,
                            "Contact $incomingNumber is NOT on the allow list. Silencing ringer."
                        )

                        // Save the current ringer mode before changing it
                        RingerModeManager.saveCurrentRingerMode(audioManager.ringerMode)

                        // Silence the ringer
                        audioManager.ringerMode = AudioManager.RINGER_MODE_SILENT
                    } else {
                        Log.i(TAG, "Contact $incomingNumber is on the allow list. Allowing call.")
                    }
                }

                TelephonyManager.EXTRA_STATE_IDLE, TelephonyManager.EXTRA_STATE_OFFHOOK -> {
                    // IDLE: The call has ended, been rejected, or was never answered.
                    // OFFHOOK: The call was answered.
                    // In either case, we should restore the original ringer mode if we changed it.

                    Log.d(TAG, "Call state is now IDLE or OFFHOOK. Restoring ringer mode.")
                    val originalMode = RingerModeManager.getOriginalRingerMode()

                    // Only restore if we have a saved state
                    if (originalMode != null) {
                        audioManager.ringerMode = originalMode
                        RingerModeManager.clearRingerMode() // Clear the saved state
                        Log.i(TAG, "Ringer mode restored to: $originalMode")
                    }
                }
            }
        }
    }

    private suspend fun isNumberOnAllowList(context: Context, incomingNumber: String): Boolean {

        val activeProfile = getActiveProfileUseCase.execute(Unit).first()
        val allowedContactIds = activeProfile.customContactsList.map { it.split("/").last() }
        val allowedPhonesMap = contactDataSource.getContactsPhones(allowedContactIds)

        return incomingNumber in allowedPhonesMap.values.flatten()


    }
}
