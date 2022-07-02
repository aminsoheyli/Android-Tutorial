package com.aminsoheyli.mvvmbasics.ui

import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aminsoheyli.mvvmbasics.R
import com.google.android.material.snackbar.Snackbar

class TutorialActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            val layoutInflater = getLayoutInflater()
            val showLayoutView = layoutInflater.inflate(R.layout.show_layout, null)
            val editText = showLayoutView.findViewById<EditText>(R.id.editTextTextPersonName)
            editText.setText("Welcome")

            Snackbar.make(it, "Snackbar", Snackbar.LENGTH_LONG)
                .setAction("Close Activity") {
                    finish()
                    val toast =
                        Toast.makeText(applicationContext, "Toast - Well done!", Toast.LENGTH_LONG)
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
                    toast.show()
                }.show()
        }
    }
}