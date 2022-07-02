package com.aminsoheyli.androidtutorial.ui

import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
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

    private var url = "https://www.google.com/"

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

        webView.webViewClient = CustomWebView()
        webView.loadUrl(url)


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
        this.url = url
        webView.loadUrl(url)
    }

    private inner class CustomWebView : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            return false
        }
    }
}