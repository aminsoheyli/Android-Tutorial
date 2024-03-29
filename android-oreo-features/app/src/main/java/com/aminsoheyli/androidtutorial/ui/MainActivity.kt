package com.aminsoheyli.androidtutorial.ui

import android.Manifest.permission.*
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_DENIED
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import com.aminsoheyli.androidtutorial.R
import com.aminsoheyli.androidtutorial.component.MyBroadcastReceiver
import com.aminsoheyli.androidtutorial.component.MyIntentService
import com.aminsoheyli.androidtutorial.component.MyJobService
import com.aminsoheyli.androidtutorial.utilities.Utility

private const val REQUEST_CODE_ASK_PERMISSIONS_FINE_LOCATION = 1
private const val REQUEST_CODE_ASK_PERMISSIONS_READ_SMS = 2

class MainActivity : AppCompatActivity() {
    private lateinit var buttonGetLocation: Button
    private lateinit var buttonToggleService: Button
    private lateinit var textViewShowLocation: TextView
    private lateinit var lm: LocationManager
    lateinit var intentService: Intent
    private val ID_JOB_SERVICE = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initServices()
        initUi()
    }

    private fun initServices() {
        lm = getSystemService(LOCATION_SERVICE) as LocationManager
        intentService = Intent(this, MyIntentService::class.java)
        // JobInfo
        val jobInfoBuilder =
            JobInfo.Builder(ID_JOB_SERVICE, ComponentName(this, MyJobService::class.java))
        jobInfoBuilder.apply {
            setPeriodic(15 * 60 * 1000L) // minimum is 15 minutes => JobInfo.MIN_PERIOD_MILLIS = 15 * 60 * 1000L;
        }
        // Send job to system
        val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.schedule(jobInfoBuilder.build())
    }

    private fun initUi() {
        buttonGetLocation = findViewById(R.id.button_show_location)
        textViewShowLocation = findViewById(R.id.textView_show_location)

        buttonGetLocation.setOnClickListener {
            showLocation()
        }

        findViewById<Button>(R.id.button_storage).setOnClickListener {
            startActivity(Intent(this, StorageActivity::class.java))
        }

        findViewById<Button>(R.id.button_web).setOnClickListener {
            startActivity(Intent(this, WebActivity::class.java))
        }

        findViewById<Button>(R.id.button_counter).setOnClickListener {
            startActivity(Intent(this, CounterActivity::class.java))
        }

        findViewById<Button>(R.id.button_content_provider).setOnClickListener {
            startActivity(Intent(this, ContentProviderActivity::class.java))
        }

        findViewById<Button>(R.id.button_media_player).setOnClickListener {
            startActivity(Intent(this, MediaPlayerActivity::class.java))
        }
        findViewById<Button>(R.id.button_notification).setOnClickListener {
            startActivity(Intent(this, NotificationActivity::class.java))
        }
        findViewById<Button>(R.id.button_broadcast).setOnClickListener {
            val intent = Intent(this, MyBroadcastReceiver::class.java)
            intent.action = "com.example.NOTIFY"
            intent.putExtra("msg", "Hello from activity")
            sendBroadcast(intent)
            readSMS()
        }

        buttonToggleService = findViewById(R.id.button_toggle_intent_service)
        buttonToggleService.text = getButtonToggleServiceStyledText("Start")
        buttonToggleService.setOnClickListener {
            var text = ""
            if (MyIntentService.isRunning) {
                text = "Start"
                stopService(intentService)
            } else {
                text = "Stop"
                startService(intentService)
            }
            buttonToggleService.text = getButtonToggleServiceStyledText(text)
            MyIntentService.isRunning = !MyIntentService.isRunning
        }

        findViewById<Button>(R.id.button_service).setOnClickListener {
            startActivity(Intent(this, ServiceActivity::class.java))
        }
    }

    private fun getButtonToggleServiceStyledText(text: String) =
        HtmlCompat.fromHtml(
            getString(R.string.button_toggle_intent_service_text, text),
            FROM_HTML_MODE_LEGACY
        )


    private fun readSMS() {
        if (Build.VERSION.SDK_INT >= 23)
            if ((checkSelfPermission(RECEIVE_SMS) != PERMISSION_GRANTED ||
                        checkSelfPermission(READ_SMS) != PERMISSION_GRANTED) &&
                !shouldShowRequestPermissionRationale(RECEIVE_SMS)
            ) {
                requestPermissions(
                    arrayOf(RECEIVE_SMS),
                    REQUEST_CODE_ASK_PERMISSIONS_READ_SMS
                )
            }
    }

    private fun showLocation() {
        // if LOCATION PERMISSION GRANTED
        if (Build.VERSION.SDK_INT >= 23)
            if (checkSelfPermission(ACCESS_FINE_LOCATION) == PERMISSION_GRANTED ||
                checkSelfPermission(ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED
            ) {
                val location =
                    lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                val message = "log: ${location?.longitude}, lat: ${location?.latitude}"
                textViewShowLocation.text = message
            } else if (!shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION) &&
                !shouldShowRequestPermissionRationale(ACCESS_COARSE_LOCATION)
            )
                requestPermissions(
                    arrayOf(
                        ACCESS_FINE_LOCATION
                    ), REQUEST_CODE_ASK_PERMISSIONS_FINE_LOCATION
                )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_ASK_PERMISSIONS_FINE_LOCATION ->
                if (grantResults[0] == PERMISSION_GRANTED)
                    showLocation()
                else
                    Utility.showSnackBar(buttonGetLocation, "You denied the location access")
            REQUEST_CODE_ASK_PERMISSIONS_READ_SMS ->
                if (grantResults[0] == PERMISSION_DENIED)
                    Utility.showSnackBar(buttonGetLocation, "You denied the sms permission")
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}