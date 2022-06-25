package com.aminsoheyli.androidtutorial

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.widget.Toast


private const val TAG = "MyBroadcastReceiver"

class MyBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val bundle = intent?.extras

        if (intent?.action.equals("com.example.NOTIFY")) {
            val msg = bundle?.getString("msg")
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
        } else if (intent?.action.equals("android.provider.Telephony.SMS_RECEIVED")) {
            if (bundle != null) {
                val pdusObj: Array<Any> = bundle.get("pdus") as Array<Any>
                val messages: Array<SmsMessage?> = arrayOfNulls(pdusObj.size)
                for (i in messages.indices) {
                    val format = bundle.getString("format").toString()
                    messages[i] = SmsMessage.createFromPdu(pdusObj[i] as ByteArray, format)
                    val senderNum = messages[i]?.originatingAddress.toString()
                    val message: String = messages[i]?.messageBody.toString()
                    Toast.makeText(context, "$senderNum: $message", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}