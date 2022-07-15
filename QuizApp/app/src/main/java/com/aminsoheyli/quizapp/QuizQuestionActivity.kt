package com.aminsoheyli.quizapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.aminsoheyli.quizapp.databinding.ActivityMainBinding
import com.aminsoheyli.quizapp.databinding.ActivityQuizQustionBinding

class QuizQustionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuizQustionBinding
    private val questions = Constants.getQuestions()
    private var currentQuestionIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizQustionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        initUi()
    }

    private fun initUi() {
        setQuestion(questions[currentQuestionIndex])
    }

    private fun setQuestion(question: Question) {
        binding.ivImage.setImageResource(question.image)
        val options = question.options
        binding.tvOptionOne.text = options[0]
        binding.tvOptionOne.text = options[1]
        binding.tvOptionOne.text = options[2]
        binding.tvOptionOne.text = options[3]
    }
}