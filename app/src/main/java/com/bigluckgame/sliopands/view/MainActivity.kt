package com.bigluckgame.sliopands.view

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.bigluckgame.sliopands.R
import com.bigluckgame.sliopands.ViewModel.LoadDataClass
import com.bigluckgame.sliopands.ViewModel.LoadFirebase
import com.facebook.FacebookSdk
import com.facebook.applinks.AppLinkData
import com.google.firebase.remoteconfig.FirebaseRemoteConfig

class MainActivity: AppCompatActivity() {
    private lateinit var settings: SharedPreferences
    private lateinit var main_url:String
    private lateinit var remoteConfig: FirebaseRemoteConfig
    private var check:Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)
        settings = getSharedPreferences("PREFS_FILE", MODE_PRIVATE);
        var url = settings.getString("URL_SETTINGS","")


        var loadData: LoadFirebase
        loadData = ViewModelProviders.of(this).get(LoadFirebase::class.java)
        loadData.load(this)
        loadData.simpleLiveData.observe(this, Observer {
            //main_url = it
            main_url = "Fetching config…"
            var e = it
        })

//        if(check == 0){
//            check = 2
//
//        }
        faceBook()
    }
    private fun faceBook(){
        FacebookSdk.setAutoInitEnabled(true)
        FacebookSdk.fullyInitialize()
        AppLinkData.fetchDeferredAppLinkData(this@MainActivity) { appLinkData ->
            Log.i("TAG_DEEP","${appLinkData?.targetUri}")
            Log.d("Lyficicle","deepLink")
            checkDeepLink(appLinkData?.targetUri.toString())
        }
    }

    private fun checkDeepLink(deep:String){
        var deepLink = deep
        //var t = main_url
        val baseUrl = "Fetching config…"

        var urlSettings = settings.getString("URL_SETTINGS","")

        if(urlSettings == null || urlSettings.equals("")){
            if(main_url == null || main_url.equals("Fetching config…") || main_url.equals("")){
                replacetoContent()
                }else{
                    if(deepLink.equals("null")){
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
        var uri = Uri.parse(url)
        var intent = Intent(this,WebViewActivity::class.java)
        intent.putExtra("URL",main_url.toString())
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
        var loadUrl = main_url
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
        //builder.query(query)
        return builder.build()
    }



    companion object{
        private const val LOADING_PHASE = "LOADING_PHASE"
    }
}