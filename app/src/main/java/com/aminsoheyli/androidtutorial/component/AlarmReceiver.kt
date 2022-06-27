package com.aminsoheyli.androidtutorial.component

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action.equals("com.example.alarm", true)){
            val bundle = intent.extras
            Toast.makeText(context, bundle?.getString("MyMessage"), Toast.LENGTH_LONG).show()
        }
    }
}