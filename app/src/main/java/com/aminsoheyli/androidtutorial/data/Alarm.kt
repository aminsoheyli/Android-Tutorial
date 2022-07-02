package com.aminsoheyli.androidtutorial.data

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.aminsoheyli.androidtutorial.component.AlarmReceiver
import java.util.*

private const val HOUR_KEY = "hour"
private const val MINUTE_KEY = "minute"


class Alarm(private val context: Context) {
    private val sharedPreference: SharedPreferences =
        context.getSharedPreferences("shared_preferences_section", Context.MODE_PRIVATE)

    fun saveAlarmData(hour: Int, minute: Int) {
        val editor = sharedPreference.edit()
        editor.putInt(HOUR_KEY, hour)
        editor.putInt(MINUTE_KEY, minute)
        editor.apply()
    }

    fun loadAlarmData() {
        val hour = sharedPreference.getInt(HOUR_KEY, 0)
        val minute = sharedPreference.getInt(MINUTE_KEY, 0)
        setAlarm(hour, minute)
    }

    fun setAlarm(hour: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)

        val intent = Intent(context, AlarmReceiver::class.java)
        intent.action = "com.example.alarm"
        intent.putExtra("MyMessage", "Hello from alarm")
        val pendingIntent = PendingIntent.getBroadcast(
            context, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }
}