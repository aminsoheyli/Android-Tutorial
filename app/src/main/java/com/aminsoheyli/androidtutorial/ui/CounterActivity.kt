package com.aminsoheyli.androidtutorial.ui

import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.aminsoheyli.androidtutorial.R

class CounterActivity : AppCompatActivity() {
    private lateinit var seekBar: SeekBar
    private lateinit var textViewCounter: TextView
    private var isRunning = false
    private val SEEKBAR_MAX_COUNTER_VALUE = 100
    private var counterValue = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counter)
        R.id.button_direct_to_web_activity
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
                    runOnUiThread {
                        seekBar.progress = counterValue
                        textViewCounter.text = counterValue.toString()
                    }
                    counterValue += 1
                    sleep(1000)
                }
            }
        }
    }
}