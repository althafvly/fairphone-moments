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

package com.fairphone.spring.launcher.ui.screen.mode

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fairphone.spring.launcher.data.model.Moment
import com.fairphone.spring.launcher.data.model.Presets
import com.fairphone.spring.launcher.ui.modeicons.fromString
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme

@Composable
fun ModeSwitcherScreen(
    currentMoment: Moment,
    onModeSelected: (Moment) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .padding(top = 50.dp, start = 24.dp, end = 24.dp),
    ) {
        Text(
            "What's your next focus?",
            fontSize = 32.sp,
            lineHeight = 38.4.sp,
            fontWeight = FontWeight.W600,
            color = Color(0xFF373737),
            textAlign = TextAlign.Center,
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 24.dp)
        ) {
            Presets.All.forEach { mode ->
                ModeSwitcherButton(
                    moment = mode,
                    isSelected = mode == currentMoment,
                    onClick = { onModeSelected(mode) },
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun ModeSwitcherButton(
    moment: Moment,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = if (isSelected) {
        ButtonDefaults.buttonColors(
            containerColor = Color.Black,
            contentColor = Color.White,
        )
    } else {
        ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black,
        )
    }
    Button(
        onClick = onClick,
        border = BorderStroke(1.dp, Color.White),
        shape = RoundedCornerShape(16.dp),
        colors = colors,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
        modifier = modifier
            .height(64.dp)
            .fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Icon(
                imageVector = ImageVector.fromString(moment.icon),
                contentDescription = moment.name,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp).size(20.dp)
            )

            Text(
                moment.name,
                fontSize = 18.sp,
                lineHeight = 21.6.sp,
                fontWeight = FontWeight(500),
                //color = colors.contentColor,
            )
        }
    }
}

@Preview
@Composable
fun ModeSwitcherButton_Preview() {
    Column {
        ModeSwitcherButton(
            moment = Presets.Essentials,
            isSelected = true,
            onClick = { /* Do nothing */ })
        ModeSwitcherButton(
            moment = Presets.Journey,
            isSelected = false,
            onClick = { /* Do nothing */ })
    }

}

@Composable
@Preview(showBackground = true)
fun ModeSwitcherScreen_Preview() {
    SpringLauncherTheme {
        ModeSwitcherScreen(
            currentMoment = Presets.Essentials
        ) {}
    }
}