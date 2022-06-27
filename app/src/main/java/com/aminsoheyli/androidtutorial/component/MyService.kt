package com.aminsoheyli.androidtutorial.component

import android.app.IntentService
import android.content.Intent

class MyService : IntentService("MyService") {
    companion object {
        var isRunning = false
    }

    override fun onHandleIntent(intent: Intent?) {
        val intentBroadcast = Intent(this, MyBroadcastReceiver::class.java)
        intentBroadcast.action = "com.example.NOTIFY"
        intentBroadcast.putExtra("msg", "Hello from service")
        while (isRunning) {
            sendBroadcast(intentBroadcast)
            Thread.sleep(2000)
        }
    }
}