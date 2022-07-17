package com.aminsoheyli.quizapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.aminsoheyli.quizapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        initUi()
    }

    private fun initUi() {
        binding.btnStart.setOnClickListener {
            val username = binding.etName.text.toString()
            if (username.isNotEmpty()) {
                val intent = Intent(this, QuizQuestionActivity::class.java)
                intent.putExtra(Constants.USER_NAME, username)
                startActivity(intent)
                finish()
            } else
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()

        }
    }
}