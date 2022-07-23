package com.aminsoheyli.a7minutesworkout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aminsoheyli.a7minutesworkout.databinding.ActivityFinishBinding

class FinishActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFinishBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinishBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarFinishActivity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolbarFinishActivity.setNavigationOnClickListener { onBackPressed() }
        binding.buttonFinish.setOnClickListener { finish() }
    }
}