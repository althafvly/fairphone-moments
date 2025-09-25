/*
 * Copyright (C) 2025 FairPhone B.V.
 *
 * SPDX-FileCopyrightText: 2025. FairPhone B.V.
 *
 * SPDX-License-Identifier: EUPL-1.2
 */

package com.fairphone.spring.launcher.data.serializer

import android.util.Log
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.fairphone.spring.launcher.data.model.protos.LauncherProfiles
import kotlinx.serialization.SerializationException
import java.io.InputStream
import java.io.OutputStream

object LauncherProfilesSerializer : Serializer<LauncherProfiles> {
    override val defaultValue: LauncherProfiles
        get() = LauncherProfiles.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): LauncherProfiles {
        return try {
            LauncherProfiles.parseFrom(input)
        } catch (e: SerializationException) {
            Log.e("LauncherProfilesSerializer", "Error deserializing proto", e)
            throw CorruptionException("Cannot read proto.", e)
        }
    }

    override suspend fun writeTo(
        t: LauncherProfiles,
        output: OutputStream
    ) {
        t.writeTo(output)
    }
}