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