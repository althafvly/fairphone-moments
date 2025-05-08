package com.fairphone.spring.launcher.ui.modeicons

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.fairphone.spring.launcher.ui.FP6Preview
import com.fairphone.spring.launcher.ui.FP6PreviewDark
import com.fairphone.spring.launcher.ui.ModeIcons
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme

enum class ModeIcon(val imageVector: ImageVector) {
    Balance(ModeIcons.Balance),
    DeepFocus(ModeIcons.DeepFocus),
    Extra1(ModeIcons.Extra1),
    Extra2(ModeIcons.Extra2),
    Extra3(ModeIcons.Extra3),
    Extra4(ModeIcons.Extra4),
    Extra5(ModeIcons.Extra5),
    Extra6(ModeIcons.Extra6),
    Recharge(ModeIcons.Recharge),
    Spring(ModeIcons.Spring);
}

fun ImageVector.Companion.fromString(string: String): ImageVector =
    ModeIcon.entries.firstOrNull { it.name == string }?.imageVector ?: ModeIcon.Spring.imageVector

@Composable
fun ModeIcon_Preview() {
    SpringLauncherTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ModeIcon.entries.forEach {
                Text("Icon : ${it.name}", color = MaterialTheme.colorScheme.onBackground)
                Image(
                    imageVector = it.imageVector,
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                )
            }
        }
    }
}

@Composable
@FP6Preview()
fun ModeIcon_LightPreview() {
    ModeIcon_Preview()
}

@Composable
@FP6PreviewDark()
fun ModeIcon_DarkPreview() {
    ModeIcon_Preview()
}


