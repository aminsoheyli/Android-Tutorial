package com.aminsoheyli.androidtutorial.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.aminsoheyli.androidtutorial.R


class NotificationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        findViewById<Button>(R.id.button_notify).setOnClickListener {
            val channelId = "notify_001"
            val notificationBuilder = NotificationCompat.Builder(this, channelId)
            notificationBuilder.setContentTitle("Danger")
                .setContentText("It will run soon")
                .setSmallIcon(R.drawable.ic_launcher_background)
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH
                )
                notificationManager.createNotificationChannel(channel)
            }
            notificationManager.notify(0, notificationBuilder.build())
        }
    }
}