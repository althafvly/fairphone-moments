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

package com.fairphone.spring.launcher.data.datasource

import android.util.Log
import androidx.datastore.core.DataStore
import com.fairphone.spring.launcher.data.model.Moment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.io.IOException

interface IMomentDataSource {
    fun getActiveMoment(): Flow<Moment>
    suspend fun updateVisibleApps(visibleApps: List<String>)
    suspend fun updateName(name: String)
}

class MomentDataSource(private val dataStore: DataStore<Moment>) : IMomentDataSource {
    override fun getActiveMoment(): Flow<Moment> {
        return dataStore.data
            .catch { exception ->
                // dataStore.data throws an IOException when an error is encountered when reading data
                if (exception is IOException) {
                    Log.e("MomentRepository", "Error reading sort order preferences.", exception)
                    emit(Moment.getDefaultInstance())
                } else {
                    throw exception
                }
            }
    }

    override suspend fun updateVisibleApps(visibleApps: List<String>) {
        dataStore.updateData { moment ->
            moment.toBuilder()
                .clearVisibleApps()
                .addAllVisibleApps(visibleApps)
                .build()
        }
    }

    override suspend fun updateName(name: String) {
        dataStore.updateData { moment ->
            moment.toBuilder()
                .setName(name)
                .build()
        }
    }

}

