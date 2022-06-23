package com.example.newslook.news.ui.activity

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.newslook.R

class WebViewActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        supportActionBar?.hide()
        val url = intent.extras!!.getString("url")
        webView = findViewById(R.id.webView)
        webView.webViewClient = WebViewClient()
        // this will load the url of the website
        webView.loadUrl(url!!)
        // this will enable the javascript settings
        webView.settings.javaScriptEnabled = true
        // this will enable the zoom on webpage
        webView.settings.setSupportZoom(true)
    }

    override fun onStop() {
        super.onStop()
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}