package com.aminsoheyli.androidtutorial

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var buttonDialog: Button
    private lateinit var buttonAlert: Button
    private lateinit var buttonSave: Button
    private lateinit var buttonLoad: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUi()
    }


    private fun initUi() {
        buttonDialog = findViewById(R.id.button_show_dialog)
        buttonAlert = findViewById(R.id.button_show_alert)

        buttonDialog.setOnClickListener {
            val popTime = PopTime()
            popTime.show(supportFragmentManager, "Show dialog fragment")
        }

        buttonAlert.setOnClickListener {
            val alert = AlertDialog.Builder(this)
            alert.setMessage("Delete document?")
                .setTitle("Confirmation")
                .setPositiveButton("Yes") { dialog, which ->
                    showSnackBar("✅ Deleted")
                }
                .setNegativeButton("No") { dialog, which ->
                    showSnackBar("❎ Canceled")
                }
            alert.show()
        }

    }

    fun setTime(time: String) {
        val text = "Time: $time";
        showSnackBar(text)
    }

    private fun showSnackBar(text: String) {
        Snackbar.make(buttonDialog, text, Snackbar.LENGTH_LONG)
            .setTextColor(Color.YELLOW)
            .show()
    }


}