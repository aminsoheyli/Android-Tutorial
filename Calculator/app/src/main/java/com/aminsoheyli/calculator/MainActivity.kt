package com.aminsoheyli.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onDigit(view: View) {}
    fun onOperator(view: View) {}
    fun onDecimalPoint(view: View) {}
    fun onClear(view: View) {}
    fun onEqual(view: View) {}
}