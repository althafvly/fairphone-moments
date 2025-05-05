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

package com.fairphone.spring.launcher.data.serializer

import android.util.Log
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.fairphone.spring.launcher.data.model.LauncherProfile
import kotlinx.serialization.SerializationException
import java.io.InputStream
import java.io.OutputStream

object LauncherProfileSerializer : Serializer<LauncherProfile> {
    override val defaultValue: LauncherProfile
        get() = LauncherProfile.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): LauncherProfile {
        return try {
            LauncherProfile.parseFrom(input)
        } catch (e: SerializationException) {
            Log.e("LauncherProfileSerializer", "Error deserializing proto", e)
            throw CorruptionException("Cannot read proto.", e)
        }
    }

    override suspend fun writeTo(
        t: LauncherProfile,
        output: OutputStream
    ) {
        t.writeTo(output)
    }
}