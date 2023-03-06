package com.example.forecast.data.service

import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.forecast.R
import com.example.forecast.data.utils.Constants.Companion.CHANNEL_ID
import com.example.forecast.data.utils.Constants.Companion.MESSAGE_EXTRA
import com.example.forecast.data.utils.Constants.Companion.NOTIFICATION_ID
import com.example.forecast.data.utils.Constants.Companion.TITLE_EXTRA

class Alert: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notifications: Notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(intent.getStringExtra(TITLE_EXTRA))
            .setContentText(intent.getStringExtra(MESSAGE_EXTRA))
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notifications)
    }
}