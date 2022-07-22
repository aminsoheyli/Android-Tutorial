package com.aminsoheyli.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import com.aminsoheyli.a7minutesworkout.databinding.ActivityExerciseBinding
import com.aminsoheyli.a7minutesworkout.databinding.ActivityMainBinding

class ExerciseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExerciseBinding
    private lateinit var restTimer: CountDownTimer
    private var restProgress = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUi()
    }

    private fun initUi() {
        setSupportActionBar(binding.toolbarExerciseActivity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarExerciseActivity.setNavigationOnClickListener {
            onBackPressed()
        }
        setRestProgressBar()
    }

    private fun setRestProgressBar() {
        binding.progressBar.progress = restProgress
        restTimer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                binding.progressBar.progress = 10 - restProgress
                binding.textViewTimer.text = (10 - restProgress).toString()
            }

            override fun onFinish() {
                Toast.makeText(this@ExerciseActivity, "Finish", Toast.LENGTH_SHORT).show()
            }
        }
        restTimer.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        restTimer.cancel()
        restProgress = 0
    }
}