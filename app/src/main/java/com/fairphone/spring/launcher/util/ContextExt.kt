/*
 * Copyright (c) 2025. Fairphone B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License
 *
 *         at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fairphone.spring.launcher.util

import android.app.role.RoleManager
import android.content.Context
import android.os.UserHandle
import android.os.UserManager

/**
 * @return [RoleManager]
 */
fun Context.roleManager() = getSystemService(Context.ROLE_SERVICE) as RoleManager

fun Context.getUserHandleFromId(userId: Int): UserHandle? {
    val userManager = getSystemService(Context.USER_SERVICE) as UserManager
    val userProfiles = userManager.userProfiles
    return userProfiles.find { it.hashCode() == userId }
}