package com.aminsoheyli.androidtutorial.ui

import android.os.Bundle
import android.webkit.WebView
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.aminsoheyli.androidtutorial.R

class WebActivity : AppCompatActivity() {
    private lateinit var buttonForward: Button
    private lateinit var buttonBack: Button
    private lateinit var buttonGo: Button
    private lateinit var editTextUrl: EditText
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        initUi()
    }

    private fun initUi() {
        webView = findViewById(R.id.webView)
        buttonGo = findViewById(R.id.button_go)
        buttonBack = findViewById(R.id.button_back)
        buttonForward = findViewById(R.id.button_forward)
        editTextUrl = findViewById(R.id.editText_url)

        // Default URL
        webView.loadUrl("https://www.varzesh3.com/")

        buttonGo.setOnClickListener {
            loadUrl(editTextUrl.text.toString())
        }

        buttonBack.setOnClickListener {
            webView.goBack()
        }

        buttonForward.setOnClickListener {
            webView.goForward()
        }
    }

    private fun loadUrl(url: String) {
        webView.loadUrl(url)
    }
}