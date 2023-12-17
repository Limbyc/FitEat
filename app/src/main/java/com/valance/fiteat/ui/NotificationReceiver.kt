package com.valance.fiteat.ui

import android.Manifest
import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.valance.fiteat.CHANNEL_ID
import com.valance.fiteat.R

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "WaterNotification") {
            val notificationManager = NotificationManagerCompat.from(context!!)
            val notification = createNotification(context)
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notificationManager.notify(1, notification)
        }
    }

    private fun createNotification(context: Context): Notification {
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_foreground)
            .setContentTitle("Reminder")
            .setContentText("Don't forget to drink water!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
    }
}
