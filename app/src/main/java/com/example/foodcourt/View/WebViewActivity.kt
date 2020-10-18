package com.example.foodcourt.View

import android.content.Intent
import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.CookieSyncManager

import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.example.foodcourt.R


class WebViewActivity(): AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // в работе с куки у меня к сожалению опыта нет,что смог на скорую руку,сделал
        // и не совсем понятно "сделать webView на телефоне,а не встроенное"
        //я сделал эту работу довольно быстро,за день,я смогу исправить если ошибки будут сильно глобальные
        setContentView(R.layout.web_view)
        CookieSyncManager.createInstance(this)
        val cookieManager: CookieManager = CookieManager.getInstance()
        val webview = WebView(this)
        webview.getSettings().setJavaScriptEnabled(true);
        cookieManager.setAcceptCookie(true)
        webview.loadUrl("https://www.nytimes.com/privacy/privacy-policy");


    }
}