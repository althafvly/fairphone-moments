package com.fairphone.spring.launcher.ui.screen.home

import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.fairphone.spring.launcher.R

@Composable
fun PermissionsScreen(context: Context) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val notificationManager =
        context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

    var canDnd by remember { mutableStateOf(false) }
    var canWrite by remember { mutableStateOf(false) }
    var canOverlay by remember { mutableStateOf(false) }

    fun refresh() {
        canDnd = notificationManager.isNotificationPolicyAccessGranted
        canWrite = Settings.System.canWrite(context)
        canOverlay = Settings.canDrawOverlays(context)
    }

    DisposableEffect(lifecycleOwner) {
        refresh() // initial check

        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                refresh()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.permissions_required),
                style = MaterialTheme.typography.headlineSmall
            )

            PermissionRow(
                title = stringResource(R.string.do_not_disturb_access),
                granted = canDnd
            ) {
                context.startActivity(
                    Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }

            PermissionRow(
                title = stringResource(R.string.modify_system_settings),
                granted = canWrite
            ) {
                context.startActivity(
                    Intent(
                        Settings.ACTION_MANAGE_WRITE_SETTINGS,
                        "package:${context.packageName}".toUri()
                    ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }

            PermissionRow(
                title = stringResource(R.string.draw_over_other_apps),
                granted = canOverlay
            ) {
                context.startActivity(
                    Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        "package:${context.packageName}".toUri()
                    ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
        }
    }
}

@Composable
private fun PermissionRow(
    title: String,
    granted: Boolean,
    onClick: () -> Unit
) {
    Card {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
               Text(text = title)
                Text(
                    text = if (granted) stringResource(R.string.permission_granted) else stringResource(
                        R.string.permission_required
                    ),
                    color = if (granted) Color(0xFF2E7D32)
                    else Color.Red,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Button(
                onClick = onClick,
                enabled = !granted
            ) {
                Text(if (granted) stringResource(R.string.done) else stringResource(R.string.grant))
            }
        }
    }
}
