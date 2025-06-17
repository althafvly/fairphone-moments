package com.fairphone.spring.launcher.ui.screen.settings.appearance

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fairphone.spring.launcher.R
import com.fairphone.spring.launcher.ui.component.SettingListItem
import com.fairphone.spring.launcher.ui.theme.FairphoneTypography

@Composable
fun AppearanceSettingsScreen(
    onCustomizeWallpaperClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = stringResource(R.string.setting_subtitle_appearance),
            style = FairphoneTypography.BodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 24.dp, start = 16.dp, end = 16.dp)
        )

        SettingListItem(
            title = stringResource(R.string.wallpaper),
            subtitle = null,
            onClick = onCustomizeWallpaperClick,
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(size = 12.dp)
                )
                .clip(RoundedCornerShape(size = 12.dp))
        )
    }
}
