package com.aminsoheyli.trelloclone.activities

import android.os.Bundle
import com.aminsoheyli.trelloclone.databinding.ActivityCardDetailsBinding

class CardDetailsActivity : BaseActivity() {
    private lateinit var binding: ActivityCardDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCardDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}