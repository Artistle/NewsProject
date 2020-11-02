package com.bigluckgame.sliopands.view

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bigluckgame.sliopands.R
import com.bigluckgame.sliopands.Utils.Timer
import com.bigluckgame.sliopands.ViewModel.LoadFirebase
import com.facebook.FacebookSdk
import com.facebook.applinks.AppLinkData
import com.onesignal.OneSignal

class MainActivity: AppCompatActivity() {
    private lateinit var settings: SharedPreferences
    //private lateinit var main_url:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        var check = 0
        settings = getSharedPreferences("PREFS_FILE", MODE_PRIVATE);
        var loadData: LoadFirebase
        loadData = ViewModelProviders.of(this@MainActivity).get(LoadFirebase::class.java)
        loadData.load(this@MainActivity)
        loadData.simpleLiveData.observe(this@MainActivity, Observer {
            var timer = Timer(object:Runnable{
                override fun run() {
                    //main_url = it
                    var s = "s"
                }
            },100,true)
            //main_url = it
            if (it != null && it != "null" && it != "" || check > 500 ){
                timer.stopTimer()
                //Log.i("Tag_Main","$main_url")
                getDeepLink(it)
            }else{
                check++
                timer.startTimer()
            }
            var s = "s"
        })
        oneS()
    }
    public fun getDeepLink(remote_config:String){
        FacebookSdk.setAutoInitEnabled(true)
        FacebookSdk.fullyInitialize()
        AppLinkData.fetchDeferredAppLinkData(this) { appLinkData ->
            checkDeepLink(appLinkData?.targetUri.toString(),remote_config)
            if(appLinkData?.targetUri == null || appLinkData?.targetUri.toString() == "null"){
                checkDeepLink(appLinkData?.targetUri.toString(),remote_config)
            }else{
                checkDeepLink(appLinkData?.targetUri.toString(),remote_config)
            }
        }
    }

    private fun checkDeepLink(deep_link:String, remote_config: String){
        var deepLink = deep_link
        val baseUrl = "Fetching config…"

        var urlSettings = settings.getString("URL_SETTINGS","")
        if(urlSettings == null || urlSettings.equals("")){
            if(baseUrl == null || baseUrl.equals("Fetching config…") || baseUrl.equals("")){
                replacetoContent()
                }else{
                    if(deepLink.equals("null") || deepLink == null){
                        Log.i("TAG"," DEP = NULL")
                        replaceToFirebase(baseUrl)
                    }else{
                        saveSettingsUrl(baseUrl,deepLink)
                        replaceToWeb()
                    }
                }
        }else{
            replaceToWeb()
        }
    }
    private fun replaceToFirebase(url:String){
        var intent = Intent(this,WebViewActivity::class.java)
        intent.putExtra("URL",url.toString())
        startActivity(intent)
    }

    private fun replaceToWeb(){
        var url = settings.getString("URL_SETTINGS","")
        var intent = Intent(this,WebViewActivity::class.java)
        intent.putExtra("URL",url)
        startActivity(intent)
    }
    private fun saveSettingsUrl(url:String,deepLink:String){
        var link = Uri.parse(deepLink)
        var loadUrl = urlBuilder(Uri.parse(url))
        var mainUrl = "$loadUrl"+"?"+link.authority
        settings = getSharedPreferences("PREFS_FILE", MODE_PRIVATE);
        var prefEditor = settings.edit()
        prefEditor.putString("URL_SETTINGS", mainUrl)
        prefEditor.apply()
    }
    private fun replacetoContent(){
        var intent = Intent(this,SplashScreenActivity::class.java)
        startActivity(intent)
    }
    private fun urlBuilder(base:Uri):Uri{
        var builder:Uri.Builder = Uri.Builder()
        builder.scheme(base.scheme)
        builder.authority(base.host)
        builder.path(base.path)
        builder.clearQuery()
        return builder.build()
    }

    fun oneS(){
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
    }
}