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

package com.fairphone.spring.launcher.ui

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "FP6",
    apiLevel = 35,
    widthDp = 372,
    heightDp = 828,
    showBackground = true,
    backgroundColor = 0xFFFFFF,
    //showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    locale = "en",
)
annotation class FP6Preview


@Preview(
    name = "FP6",
    apiLevel = 35,
    widthDp = 372,
    heightDp = 828,
    showBackground = true,
    backgroundColor = 0x000000,
    //showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    locale = "en",
)
annotation class FP6PreviewDark