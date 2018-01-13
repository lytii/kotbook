package com.book.clue.kotbook

import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebViewClient


class WebActivity : Activity() {
    val GRAVITY_TALES_URL = "http://gravitytales.com"

    @TargetApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val url = GRAVITY_TALES_URL + intent.extras.getString("bookUrl")

        val webview = WebView(applicationContext)
        val settings = webview.settings
        settings.javaScriptEnabled = true
        webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val i = Intent()
                i.putExtra("bookUrl", request?.url)
                setResult(RESULT_OK, i)
                finish()
                return false
//                return super.shouldOverrideUrlLoading(view, request)
            }
        }
        setContentView(webview)
        webview.loadUrl(url)
    }
}