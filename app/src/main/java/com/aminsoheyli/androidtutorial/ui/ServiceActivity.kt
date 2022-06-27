package com.aminsoheyli.androidtutorial.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aminsoheyli.androidtutorial.R
import com.aminsoheyli.androidtutorial.component.LocalService
import com.aminsoheyli.androidtutorial.component.LocalService.LocalBinder


class ServiceActivity : AppCompatActivity() {
    var mService: LocalService? = null
    var mBound = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service)
    }

    override fun onStart() {
        super.onStart()
        // Bind to LocalService
        val intent = Intent(this, LocalService::class.java)
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection)
            mBound = false
        }
    }

    fun onButtonClick(view: View) {
        if (mBound) {
            // Call a method from the LocalService.
            // However, if this call were something that might hang, then this request should
            // occur in a separate thread to avoid slowing down the activity performance.
            val num = mService!!.randomNumber
            Toast.makeText(this, "number: $num", Toast.LENGTH_SHORT).show()
        }
    }

    /** Defines callbacks for service binding, passed to bindService()  */
    private val mConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            val binder = service as LocalBinder
            mService = binder.service
            mBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }
}