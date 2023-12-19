package com.valance.fiteat

import dagger.hilt.android.HiltAndroidApp
import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

@HiltAndroidApp
class FitEatApp : Application() {

        companion object {
                lateinit var instance: FitEatApp
                        private set
        }

        override fun onCreate() {
                super.onCreate()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                        val channel = NotificationChannel(
                                "channelID",
                                "Channel_name",
                                NotificationManager.IMPORTANCE_HIGH
                        )

                        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        manager.createNotificationChannel(channel)
                }
                instance = this
        }

}
