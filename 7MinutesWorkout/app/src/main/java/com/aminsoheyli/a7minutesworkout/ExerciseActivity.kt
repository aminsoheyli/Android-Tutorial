package com.aminsoheyli.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import com.aminsoheyli.a7minutesworkout.databinding.ActivityExerciseBinding

class ExerciseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExerciseBinding
    private lateinit var restTimer: CountDownTimer
    private var progressValue = 0

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
        setupRest()
    }

    private fun setupExercise() {
        binding.textViewTitle.text = "Exercise Name"
        setProgressBar(30000, 30) { setupRest() }
    }

    private fun setupRest() {
        binding.textViewTitle.text = "GET READY FOR"
        setProgressBar(10000, 10) { setupExercise() }
    }

    private fun setProgressBar(
        duration: Long,
        maxProgressValue: Int,
        onFinishFunction: () -> Any
    ) {
        binding.progressBar.max = maxProgressValue
        binding.progressBar.progress = progressValue
        restTimer = object : CountDownTimer(duration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                progressValue++
                val progress = maxProgressValue - progressValue
                binding.progressBar.progress = progress
                binding.textViewTimer.text = progress.toString()
            }

            override fun onFinish() {
                restTimer.cancel()
                progressValue = 0
                onFinishFunction()
            }
        }.start()
    }
}