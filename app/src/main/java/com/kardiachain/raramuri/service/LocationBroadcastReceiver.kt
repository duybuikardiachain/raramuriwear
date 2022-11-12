package com.kardiachain.raramuri.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.localbroadcastmanager.content.LocalBroadcastManager

@Composable
fun LocationBroadcastReceiver(
    systemAction: String,
    onLocationEvent: (intent: Intent) -> Unit
) {

    val context = LocalContext.current
    val systemEvent by rememberUpdatedState(onLocationEvent)

    DisposableEffect(context, systemEvent) {
        val intentFilter = IntentFilter(systemAction)

        val broadcast = object : BroadcastReceiver() {
            override fun onReceive(
                context: Context,
                intent: Intent
            ) {
                onLocationEvent(intent)
            }
        }

        LocalBroadcastManager.getInstance(context).registerReceiver(broadcast, intentFilter)

        onDispose {
            LocalBroadcastManager.getInstance(context).unregisterReceiver(broadcast)
        }
    }
}