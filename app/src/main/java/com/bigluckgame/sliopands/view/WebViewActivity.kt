package com.bigluckgame.sliopands.view

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import com.bigluckgame.sliopands.R


class WebViewActivity(): AppCompatActivity() {
    //shared для сохранения ссылки,временное решение до кэширования
    private lateinit var settings:SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.web_view)

        var url = intent.getStringExtra("URL")

        CookieSyncManager.createInstance(this)

        settings = getSharedPreferences("PREFS_FILE", MODE_PRIVATE);
        var y = settings.getString("URL_SETTINGS","")

        val cookieManager: CookieManager = CookieManager.getInstance()
        val webview = findViewById<WebView>(R.id.main_web_view)
        webview.settings.setAppCacheEnabled(true)
        webview.settings.setAppCachePath(this.getCacheDir().getPath())
        webview.settings.setCacheMode(WebSettings.LOAD_DEFAULT)
        webview.settings.javaScriptEnabled = true
        webview.settings.domStorageEnabled = true
        webview.settings.databaseEnabled = true

        var t = this.getCacheDir().getPath()
        var r = webview.settings.cacheMode.toString()
        cookieManager.setAcceptCookie(true)
        webview.loadUrl(url.toString());
        webview.setWebViewClient(object : WebViewClient(){} )
        webview.setWebChromeClient(object:  WebChromeClient() {} )
    }

    fun saveUrl(url:String){
        settings = getSharedPreferences("PREFS_FILE", MODE_PRIVATE);
        var prefEditor = settings.edit()
        prefEditor.putString("URL_SETTINGS", url)
        prefEditor.apply()
    }

    companion object{
        private const val TAG = "MainActivity"
        private const val LOADING_PHASE = "test_url"
    }
}