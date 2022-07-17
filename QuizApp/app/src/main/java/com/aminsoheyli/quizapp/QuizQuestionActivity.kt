package com.aminsoheyli.quizapp

import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.aminsoheyli.quizapp.databinding.ActivityQuizQustionBinding

class QuizQuestionActivity : AppCompatActivity() {
    companion object {
        val NUMBER_OF_QUESTIONS = 10
    }

    private lateinit var binding: ActivityQuizQustionBinding
    private val questions = Constants.getQuestions()
    private var currentQuestionIndex = 0
    private var selectedOptionPosition = -1

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
        binding.btnSubmit.setOnClickListener {
            if (selectedOptionPosition == -1) {
                if (currentQuestionIndex < questions.size - 1)
                    setQuestion(questions[++currentQuestionIndex])
                else {
                    binding.btnSubmit.text = "FINISH"
                    Toast.makeText(
                        this,
                        "You have successfully completed the Quiz",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                val question = questions[currentQuestionIndex]
                if (question.correctAnswer != selectedOptionPosition)
                    answerView(selectedOptionPosition, R.drawable.wrong_option_border_bg)
                answerView(question.correctAnswer, R.drawable.correct_option_border_bg)
                if (currentQuestionIndex == questions.size - 1)
                    binding.btnSubmit.text = "FINISH"
                else
                    binding.btnSubmit.text = "Next Question"
                selectedOptionPosition = -1
            }
        }
        binding.tvOptionOne.setOnClickListener { onOptionClicked(it) }
        binding.tvOptionTwo.setOnClickListener { onOptionClicked(it) }
        binding.tvOptionThree.setOnClickListener { onOptionClicked(it) }
        binding.tvOptionFour.setOnClickListener { onOptionClicked(it) }
    }

    private fun answerView(answer: Int, drawableView: Int) {
        when (answer) {
            0 -> binding.tvOptionOne.background = ContextCompat.getDrawable(this, drawableView)
            1 -> binding.tvOptionTwo.background = ContextCompat.getDrawable(this, drawableView)
            2 -> binding.tvOptionThree.background = ContextCompat.getDrawable(this, drawableView)
            else -> binding.tvOptionFour.background = ContextCompat.getDrawable(this, drawableView)
        }
    }

    private fun onOptionClicked(v: View) {
        when (v.id) {
            R.id.tv_option_one -> selectedOptionView(v as TextView, 0)
            R.id.tv_option_two -> selectedOptionView(v as TextView, 1)
            R.id.tv_option_three -> selectedOptionView(v as TextView, 2)
            else -> selectedOptionView(v as TextView, 3)
        }
    }

    private fun setQuestion(question: Question) {
        defaultOptionsView()
        binding.btnSubmit.text = "SUBMIT"

        binding.ivImage.setImageResource(question.image)

        binding.progressBar.progress = currentQuestionIndex + 1
        binding.tvProgress.text = "${currentQuestionIndex + 1}/${binding.progressBar.max}"

        val options = question.options
        binding.tvOptionOne.text = options[0]
        binding.tvOptionTwo.text = options[1]
        binding.tvOptionThree.text = options[2]
        binding.tvOptionFour.text = options[3]
    }

    private fun defaultOptionsView() {
        val options = ArrayList<TextView>()
        options.add(binding.tvOptionOne)
        options.add(binding.tvOptionTwo)
        options.add(binding.tvOptionThree)
        options.add(binding.tvOptionFour)
        for (option in options)
            setTypeFace(option, "#7A8089", Typeface.NORMAL, R.drawable.default_option_border_bg)
    }

    private fun selectedOptionView(tv: TextView, selectedOptionNum: Int) {
        defaultOptionsView()
        selectedOptionPosition = selectedOptionNum

        setTypeFace(tv, "#363A43", Typeface.BOLD, R.drawable.selected_option_border_bg)
    }

    private fun setTypeFace(tv: TextView, color: String, style: Int, backgroundDrawable: Int) {
        tv.setTextColor(Color.parseColor(color))
        tv.setTypeface(Typeface.DEFAULT, style)
        tv.background = ContextCompat.getDrawable(this, backgroundDrawable)
    }
}


