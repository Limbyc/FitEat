package com.valance.fiteat.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.valance.fiteat.R

class WaterReminderWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    private val channelID = "channelID"
    private var currentNotificationID = 1

    override fun doWork(): Result {
        val title = "Не забывайте пить воду!!"
        val message = "Вода - источник жизни. Не забудьте попить!"

        createNotificationChannel()
        sendNotification(title, message)
        return Result.success()
    }

    private fun createNotificationChannel() {
        val name = "Notification Channel"
        val desc = "A description of the channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID, name, importance)
        channel.description = desc

        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun sendNotification(title: String, message: String) {
        val notification = NotificationCompat.Builder(applicationContext, channelID)
            .setSmallIcon(R.mipmap.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .build()

        val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(currentNotificationID++, notification)
    }
}


