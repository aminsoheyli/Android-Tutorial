package com.aminsoheyli.androidtutorial.component

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.aminsoheyli.androidtutorial.data.Alarm

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action.equals("com.example.alarm", true)) {
            val bundle = intent.extras
            Toast.makeText(context, bundle?.getString("MyMessage"), Toast.LENGTH_LONG).show()
        } else if (intent.action.equals("android.intent.action.BOOT_COMPLETED")) {
            val alarm = Alarm(context)
            alarm.loadAlarmData()
        }
    }
}