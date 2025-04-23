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

package com.fairphone.spring.launcher.ui.screen.home

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fairphone.spring.launcher.R
import com.fairphone.spring.launcher.activity.LauncherSettingsActivity
import com.fairphone.spring.launcher.data.model.AppInfo
import com.fairphone.spring.launcher.ui.FP6Preview
import com.fairphone.spring.launcher.ui.FP6PreviewDark
import com.fairphone.spring.launcher.ui.theme.FairphoneTypography
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme
import org.koin.androidx.compose.koinViewModel
import java.time.format.DateTimeFormatter

const val CLOCK_TIME_FORMAT = "HH:mm"
const val CLOCK_DATE_FORMAT = "EEE, dd LLL"

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val dateTime by viewModel.dateTime.collectAsStateWithLifecycle()
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    val (date, time) = remember(dateTime) {
        dateTime.format(DateTimeFormatter.ofPattern(CLOCK_DATE_FORMAT)) to
                dateTime.format(DateTimeFormatter.ofPattern(CLOCK_TIME_FORMAT))
    }

    screenState ?: return

    HomeScreen(
        date = date,
        time = time,
        modeButtonIcon = Icons.Filled.Settings,
        modeButtonText = screenState!!.activeMoment.name,
        appList = screenState!!.visibleApps,
        onAppClick = { appInfo ->
            viewModel.onAppClick(context, appInfo)
        },
        onModeSwitcherButtonClick = {
            LauncherSettingsActivity.start(context)
        }
    )
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    date: String,
    time: String,
    modeButtonIcon: ImageVector,
    modeButtonText: String,
    appList: List<AppInfo>,
    onAppClick: (AppInfo) -> Unit,
    onModeSwitcherButtonClick: () -> Unit,
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

            OutlinedButton(
                onClick = onModeSwitcherButtonClick,
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurfaceVariant),
                shape = RoundedCornerShape(size = 16.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(size = 16.dp))
                    .padding(top = 12.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = modeButtonIcon,
                        contentDescription = null,
                        modifier = Modifier.size(ButtonDefaults.IconSize),
                        tint = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = modeButtonText,
                        style = FairphoneTypography.ButtonDefault,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }

        }

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
            style = FairphoneTypography.AppButtonDefault,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
@FP6Preview
fun HomeScreen_Preview() {
    SpringLauncherTheme {
        HomeScreen(
            date = "Wed, 13 Feb",
            time = "12:30",
            modeButtonIcon = Icons.Filled.Settings,
            modeButtonText = "Essentials",
            appList = previewAppList(LocalContext.current),
            onAppClick = {},
            onModeSwitcherButtonClick = {},
        )
    }
}

@Composable
@FP6PreviewDark
fun HomeScreenDark_Preview() {
    SpringLauncherTheme {
        HomeScreen(
            date = "Wed, 13 Feb",
            time = "12:30",
            modeButtonIcon = Icons.Filled.Settings,
            modeButtonText = "Essentials",
            appList = previewAppList(LocalContext.current),
            onAppClick = {},
            onModeSwitcherButtonClick = {},
        )
    }
}

fun previewAppList(context: Context) = listOf(
    AppInfo(
        name = "Phone",
        packageName = "com.package.app1",
        mainActivityClassName = "",
        userUuid = 0,
        icon = ContextCompat.getDrawable(context, R.drawable.ic_launcher_foreground)!!,
    ),
    AppInfo(
        name = "Chrome",
        packageName = "com.package.app2",
        mainActivityClassName = "",
        userUuid = 0,
        icon = ContextCompat.getDrawable(context, R.drawable.ic_launcher_foreground)!!,
    ),
    AppInfo(
        name = "Messages",
        packageName = "com.package.app3",
        mainActivityClassName = "",
        userUuid = 0,
        icon = ContextCompat.getDrawable(context, R.drawable.ic_launcher_foreground)!!,
    ),
    AppInfo(
        name = "Camera",
        packageName = "com.package.app4",
        mainActivityClassName = "",
        userUuid = 0,
        icon = ContextCompat.getDrawable(context, R.drawable.ic_launcher_foreground)!!,
    ),
    AppInfo(
        name = "Maps",
        packageName = "com.package.app5",
        mainActivityClassName = "",
        userUuid = 0,
        icon = ContextCompat.getDrawable(context, R.drawable.ic_launcher_foreground)!!,
    ),
)