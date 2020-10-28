package com.example.foodcourt.view

import android.os.Bundle
import android.util.Log
import android.webkit.*

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.example.foodcourt.R
import kotlinx.android.synthetic.main.web_view.*

class WebViewActivity(): AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.web_view)


        var url = intent.getStringExtra("URL")
        Log.i("TAG URL",url.toString())
        CookieSyncManager.createInstance(this)
        val cookieManager: CookieManager = CookieManager.getInstance()
        val webview = findViewById<WebView>(R.id.main_web_view)
        webview.settings.setAppCacheEnabled(true)
        webview.settings.javaScriptEnabled = true
        webview.settings.domStorageEnabled = true
        webview.settings.databaseEnabled = true
        webview.settings.setAppCachePath(this.getCacheDir().getPath());
        webview.settings.setCacheMode(WebSettings.LOAD_DEFAULT)

        var t = webview.settings.cacheMode
        var r = webview.settings.cacheMode.toString()
        cookieManager.setAcceptCookie(true)
        webview.loadUrl(url.toString());
        webview.setWebViewClient(object : WebViewClient(){} )
        webview.setWebChromeClient(object:  WebChromeClient() {} )
    }

    companion object{
        private const val TAG = "MainActivity"
        private const val LOADING_PHASE = "test_url"
        private const val WELCOME_MESSAGE_CAPS = "welcome_message_caps"
        private const val WELCOME_MESSAGE = "welcome_message"
    }
}