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

package com.fairphone.spring.launcher.ui.screen.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fairphone.spring.launcher.data.model.AppInfo
import com.fairphone.spring.launcher.ui.theme.FairphoneTypography
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme
import java.time.format.DateTimeFormatter

const val CLOCK_TIME_FORMAT = "HH:mm"
const val CLOCK_DATE_FORMAT = "EEE, dd LLL"

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel = viewModel(factory = HomeScreenViewModel.Factory)
) {
    val state by viewModel.screenState.collectAsStateWithLifecycle()
    val date = remember(state.dateTime) {
        state.dateTime.format(DateTimeFormatter.ofPattern(CLOCK_DATE_FORMAT))
    }
    val time = remember(state.dateTime) {
        state.dateTime.format(DateTimeFormatter.ofPattern(CLOCK_TIME_FORMAT))
    }

    HomeScreen(
        modifier = modifier,
        date = date,
        time = time,
        appList = state.appList,
        onAppClick = { appInfo ->
            viewModel.onAppClick(appInfo)
        }
    )
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    date: String,
    time: String,
    appList: List<AppInfo>,
    onAppClick: (AppInfo) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp)
        ) {
            Text(
                text = time,
                style = FairphoneTypography.Time,
                color = MaterialTheme.colorScheme.onBackground,
            )

            Text(
                text = date,
                style = FairphoneTypography.Date,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
        // Mode Switcher Button - hidden until modes are implemented
        /*Button(
            onClick = onModeSwitcherButtonClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0x80FFFFFF),
                contentColor = Color.Black,
            ),
            border = BorderStroke(1.dp, Color.White),
            shape = RoundedCornerShape(size = 16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
            modifier = Modifier
                .clip(RoundedCornerShape(size = 16.dp))
            //.background(color = Color(0x80FFFFFF))


        ) {
            Icon(
                imageVector = Icons.Filled.Settings, // Replace with your actual icon
                contentDescription = null,
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text(
                text = currentMode.name,
                fontSize = 18.sp,
                lineHeight = 18.sp,
            )
        }*/

        Spacer(modifier = Modifier.weight(1f))

        AppList(
            appList = appList,
            onAppClick = onAppClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        )
    }
}

@Composable
fun AppList(
    appList: List<AppInfo>,
    onAppClick: (AppInfo) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        appList.forEach { app ->
            AppButton(
                appName = app.name,
                onAppClick = { onAppClick(app) }
            )
        }
    }
}

@Composable
fun AppButton(appName: String, onAppClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onAppClick() }
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = appName,
            style = FairphoneTypography.ButtonDefault,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
@Preview(showBackground = true)
fun HomeScreen_Preview() {
    SpringLauncherTheme {
        HomeScreen(
            date = "Wed, 13 Feb",
            time = "12:30",
            appList = listOf(
                AppInfo(
                    name = "Phone",
                    packageName = "com.package.app1",
                    mainActivityClassName = "",
                    userUuid = 0
                ),
                AppInfo(
                    name = "Chrome",
                    packageName = "com.package.app2",
                    mainActivityClassName = "",
                    userUuid = 0
                ),
                AppInfo(
                    name = "Messages",
                    packageName = "com.package.app3",
                    mainActivityClassName = "",
                    userUuid = 0
                ),
                AppInfo(
                    name = "Camera",
                    packageName = "com.package.app4",
                    mainActivityClassName = "",
                    userUuid = 0
                ),
                AppInfo(
                    name = "Maps",
                    packageName = "com.package.app5",
                    mainActivityClassName = "",
                    userUuid = 0
                ),
            ),
            onAppClick = {}
        )
    }
}