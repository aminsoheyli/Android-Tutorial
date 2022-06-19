package com.aminsoheyli.androidtutorial

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var buttonDialog: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUi()
    }


    private fun initUi() {
        buttonDialog = findViewById(R.id.button_show)
        buttonDialog.setOnClickListener {
            val popTime = PopTime()
            popTime.show(supportFragmentManager, "Show dialog fragment")
        }
    }

    fun setTime(time: String) {
        Snackbar.make(
            buttonDialog,
            "Time: $time",
            Snackbar.LENGTH_LONG
        ).setTextColor(Color.YELLOW)
            .show()
    }


}