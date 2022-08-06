package com.aminsoheyli.trelloclone.activities

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.aminsoheyli.trelloclone.databinding.ActivitySplashBinding
import com.aminsoheyli.trelloclone.firebase.Firestore


class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUi()

        Handler(Looper.getMainLooper()).postDelayed({
            val currentUserId = Firestore().getCurrentUserID()
            if (currentUserId.isNotEmpty())
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            else
                startActivity(Intent(this@SplashActivity, IntroActivity::class.java))
            finish()
        }, 2000)
    }

    private fun initUi() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        val typeFace = Typeface.createFromAsset(assets, "carbon bl.ttf")
        binding.tvAppName.typeface = typeFace
    }
}