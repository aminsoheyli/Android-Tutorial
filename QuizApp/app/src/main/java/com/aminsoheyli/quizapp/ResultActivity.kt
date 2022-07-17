package com.aminsoheyli.quizapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aminsoheyli.quizapp.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initUi()
    }

    private fun initUi() {
        val username = intent.getStringExtra(Constants.USER_NAME)
        val totalQuestions = intent.getIntExtra(Constants.TOTAL_QUESTIONS, 1)
        val correctAnswers = intent.getIntExtra(Constants.CORRECT_ANSWERS, 0)

        binding.tvName.text = username
        binding.tvScore.text = "Your score is $correctAnswers/$totalQuestions"
        binding.btnFinish.setOnClickListener { startActivity(Intent(this,MainActivity::class.java)) }
    }
}