package com.aminsoheyli.androidtutorial.ui

import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.aminsoheyli.androidtutorial.R

class WebActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        initUi()
    }

    private fun initUi() {
        val webView = findViewById<WebView>(R.id.webView)
    }
}