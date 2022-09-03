package com.aminsoheyli.databinding.components.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.aminsoheyli.databinding.databinding.ActivityMainBinding
import com.aminsoheyli.databinding.databinding.ActivityMainMaterialBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainMaterialBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // View-Binding initialization
        binding = ActivityMainMaterialBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUi()
    }

    private fun initUi() {
        // View-Binding use-case
        binding.editTextName.addTextChangedListener { watcher ->
            binding.textViewBindingMessage.text = watcher.toString().trim()
        }
    }
}