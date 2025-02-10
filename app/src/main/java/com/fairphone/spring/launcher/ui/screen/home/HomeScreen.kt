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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fairphone.spring.launcher.data.model.AppInfo
import com.fairphone.spring.launcher.data.model.Mode
import com.fairphone.spring.launcher.data.model.Presets
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = viewModel()
) {

    val state by viewModel.screenState.collectAsStateWithLifecycle()

    HomeScreen(
        currentMode = Presets.Spring,
        onModeSwitcherButtonClick = {

        },
        appList = state.appList,
        onAppClick = { appInfo ->
            viewModel.onAppClick(appInfo)
        }
    )
}

@Composable
fun HomeScreen(
    currentMode: Mode,
    onModeSwitcherButtonClick: () -> Unit,
    appList: List<AppInfo>,
    onAppClick: (AppInfo) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            "12:30",
            fontSize = 44.sp,
            lineHeight = 44.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
        )

        Text(
            "Mon, 26 nov",
            fontSize = 22.sp,
            lineHeight = 22.sp,
            color = Color.Black,
        )


        Button(
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
        }

        Spacer(modifier = Modifier.weight(1f))

        AppList(
            appList = appList,
            onAppClick = onAppClick,
            modifier = Modifier
                .padding(bottom = 24.dp)
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
        modifier = modifier.fillMaxWidth()
    ) {
        appList.forEach { app ->
            AppItem(
                appName = app.name,
                onAppClick = { onAppClick(app) }
            )
        }
    }
}

@Composable
fun AppItem(appName: String, onAppClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onAppClick() }
    ) {
        Text(
            appName,
            style = TextStyle(
                fontSize = 24.sp,
                lineHeight = 28.8.sp,
                fontWeight = FontWeight(500),
                color = Color(0xFF2E2E2E),
                textAlign = TextAlign.Center,
            ),
            modifier = Modifier
                .padding(vertical = 8.dp)
                .align(Alignment.Center)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreen_Preview() {
    SpringLauncherTheme {
        HomeScreen(
            //currentMode = Presets.Spring,
            //ModeSwitcherButtonClick = {},
        )
    }
}