package com.aminsoheyli.quizapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class QuizQustionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_qustion)
        val questions = Constants.getQuestions()
    }
}