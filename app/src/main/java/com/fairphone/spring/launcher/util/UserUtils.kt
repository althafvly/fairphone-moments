/*
 * Copyright (C) 2025 FairPhone B.V.
 *
 * SPDX-FileCopyrightText: 2025. FairPhone B.V.
 *
 * SPDX-License-Identifier: EUPL-1.2
 */

package com.fairphone.spring.launcher.util

import android.content.Context
import android.os.UserHandle
import android.os.UserManager
import android.util.Log

/**
 * Checks if the given user is a managed profile.
 *
 * @param context The context to use.
 * @return True if the user is a managed profile, false otherwise.
 * @see UserManager.isManagedProfile
 * Attention: This function uses reflection to access UserManager.isManagedProfile()
 */
fun UserHandle.isManagedProfile(context: Context): Boolean {
    return try {
        val userManager = context.getSystemService(Context.USER_SERVICE) as UserManager
        val isManagedProfile = UserManager::class.java
            .getDeclaredMethod("isManagedProfile", Int::class.java)
        isManagedProfile.invoke(userManager, this@isManagedProfile.hashCode()) as Boolean
    } catch (e: Exception) {
        Log.w("UserUtils", "Failed to check if user is work profile", e)
        false
    }
}