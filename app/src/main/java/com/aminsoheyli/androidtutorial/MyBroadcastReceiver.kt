package com.aminsoheyli.androidtutorial

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

private const val TAG = "MyBroadcastReceiver"

class MyBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action.equals("com.example.NOTIFY")) {
            val bundle = intent?.extras
            val msg = bundle?.getString("msg")
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
        }
    }
}