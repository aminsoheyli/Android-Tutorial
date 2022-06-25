package com.aminsoheyli.androidtutorial.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.aminsoheyli.androidtutorial.R

const val KEY_COUNTER = "COUNTER"

private const val SEEKBAR_MAX_COUNTER_VALUE = 100

class CounterActivity : AppCompatActivity() {
    private lateinit var seekBar: SeekBar
    private lateinit var textViewCounter: TextView
    private lateinit var handler: Handler
    private var isRunning = false
    private var counterValue = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counter)
        R.id.button_web
        handler = CounterHandler()
        initUi()
    }

    private fun initUi() {
        seekBar = findViewById(R.id.seekBar)
        textViewCounter = findViewById(R.id.textViewCounter)
        seekBar.max = SEEKBAR_MAX_COUNTER_VALUE
        findViewById<Button>(R.id.buttonStart).setOnClickListener {
            isRunning = true
            val counterThread = CounterThread()
            counterThread.start()
        }

        findViewById<Button>(R.id.buttonStop).setOnClickListener {
            isRunning = false
        }
    }

    private inner class CounterThread : Thread() {
        override fun run() {
            super.run()
            while (isRunning) {
                if (counterValue <= SEEKBAR_MAX_COUNTER_VALUE) {
                    /*
                    runOnUiThread {
                        seekBar.progress = counterValue
                        textViewCounter.text = counterValue.toString()
                    }
                    */
                    val msg = handler.obtainMessage()
                    val bundle = Bundle()
                    bundle.putInt(KEY_COUNTER, counterValue)
                    msg.data = bundle
                    handler.sendMessage(msg)

                    counterValue += 1
                    sleep(1000)
                }
            }
        }
    }

    @SuppressLint("HandlerLeak")
    private inner class CounterHandler : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            val count: Int = msg.data.getInt(KEY_COUNTER)
            seekBar.progress = count
            textViewCounter.text = count.toString()
        }

    }
}