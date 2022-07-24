package com.aminsoheyli.a7minutesworkout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aminsoheyli.a7minutesworkout.databinding.ActivityBmiBinding

class BmiActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBmiBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarBmiActivity)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "BMI"
        }
        binding.toolbarBmiActivity.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}