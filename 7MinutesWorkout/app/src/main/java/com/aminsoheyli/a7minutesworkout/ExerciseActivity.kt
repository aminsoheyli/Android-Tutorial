package com.aminsoheyli.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aminsoheyli.a7minutesworkout.databinding.ActivityExcersiceBinding
import com.aminsoheyli.a7minutesworkout.databinding.ActivityMainBinding

class ExerciseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExcersiceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExcersiceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUi()
    }

    private fun initUi() {
        setSupportActionBar(binding.toolbarExerciseActivity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarExerciseActivity.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}