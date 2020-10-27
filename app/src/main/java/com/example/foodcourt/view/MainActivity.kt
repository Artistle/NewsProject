package com.example.foodcourt.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import android.webkit.WebView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.foodcourt.R
import com.facebook.FacebookSdk
import com.facebook.applinks.AppLinkData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.onesignal.OneSignal


/*
*
* Задача довольно простая и типичная,я не стал сильно усложнять её архитектурой,многопоточностью,так-как retrofit работает в фоновом потоке
* так-же мало работы с view элементами,поэтому я отсеил data binding,в общем старался сделать как можно проще,нежели усложнять и раздувать код.
* единственное что я запорол работу webView,как мне кажется,т.к. раньше с куки не работал,и всё что нашёл,выложил в коде,но эту тему я пожалуй подтяну на днях
* Facebook SDK я интегрировал,но если верить документации,регистрация скачиваний происходит автоматически
*на эту работу у меня шёл день(воскресенье)
*
* */

class MainActivity : AppCompatActivity() {
    private lateinit var remoteConfig: FirebaseRemoteConfig
    private lateinit var base_url:Uri

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.web_view)
        FacebookSdk.setAutoInitEnabled(true)
        FacebookSdk.fullyInitialize()
        AppLinkData.fetchDeferredAppLinkData(
                this@MainActivity
        ) { appLinkData ->
            if (appLinkData != null || appLinkData?.targetUri != null) {
                Log.i("TAG","${appLinkData.targetUri}")
                fetchWelcome(appLinkData.targetUri.toString())
            } else {
                fetchWelcome(appLinkData?.targetUri.toString())
            }
        }
//        var manager: FragmentManager = supportFragmentManager
//        var loadData:LoadDataClass
//        recyclerView = this.findViewById<RecyclerView>(R.id.main_recycler)
//        loadData = ViewModelProviders.of(this).get(LoadDataClass::class.java)
//        loadData.loadData()
//        loadData.simpleLiveData.observe(this,Observer{
//
//            var adapter1 =  it?.let { it1 -> RecyclerViewAdapter(it1,manager) }
//            recyclerView.setHasFixedSize(true)
//            val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(getApplicationContext())
//            recyclerView.layoutManager = layoutManager
//            recyclerView.setAdapter(adapter1)
//        })
//
//        /*не знал как и куда вставить webView и поэтому решил просто сделать кнопку перехода*/
//        var intent = Intent(this,WebViewActivity::class.java)
//
//        var fab = findViewById<FloatingActionButton>(R.id.fab)
//        fab.setOnClickListener(object : View.OnClickListener{
//            override fun onClick(v: View?) {
//                startActivity(intent)
//            }
//        })
        val channelId = getString(R.string.default_notification_channel_id)
        val channelName = getString(R.string.default_notification_channel_name)
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager?.createNotificationChannel(NotificationChannel(channelId,
            channelName, NotificationManager.IMPORTANCE_LOW))

        intent.extras?.let {
            for (key in it.keySet()) {
                val value = intent.extras?.get(key)
                Log.d("TAG", "Key: $key Value: $value")
            }
        }

        //я думаю эту логику firebase лучше хранить в viewModel,но поскольку я работаю с ней впервые,оставлю здесь,и буду дальше изучать эту тему
        // и искать архитектурные решения для неё
        //я так и не понял надо только интегрировать,или выводить пуш уведомления,поэтому я просто добавил,и проверил в дебаге,оповещения работают и приходят
        Firebase.messaging.getToken().addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            val token = task.result
            val msg = getString(R.string.msg_token_fmt, token)
            Log.d("TAG", msg)
        })
        Firebase.messaging.subscribeToTopic("weather").addOnCompleteListener{task ->
            var msg = getString(R.string.msg_subscribed)
            if (!task.isSuccessful) {
                msg = getString(R.string.msg_subscribe_failed)
            }
            Log.d("TAG", msg)
            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        }

        oneS()
    }

    fun oneS(){
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        OneSignal.startInit(this)
            .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
            .unsubscribeWhenNotificationsAreDisabled(true)
            .init();
    }
    fun fetchWelcome(deepLinkUrl:String){
        var remote:Uri
        var query = Uri.parse(deepLinkUrl)
        var mainUrl:String
        var url:Uri

        remoteConfig = Firebase.remoteConfig
        var configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 100
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.default_resource_config)
        remoteConfig.fetchAndActivate()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val updated = task.result
                        Log.i("TAG", "Config params updated: $updated")

                    } else {
                        Log.i("TAG", "Config params updated: ${task.exception}")
                    }
                }

        val baseUrl = remoteConfig[LOADING_PHASE].asString()
        remote = Uri.parse(baseUrl)

        if(deepLinkUrl == null || deepLinkUrl.equals("") || deepLinkUrl.equals("null")){
            url = builderUrl(base_url,base_url.query.toString())
            var t = Uri.parse(url.query)
            var r = t.query
            t.query
        }else{
            Log.i("DEEP link","$deepLinkUrl")
            url = builderUrl(base_url,query.query.toString())
        }
        Log.i("Ready Url","$url")

        CookieSyncManager.createInstance(this)
        val cookieManager: CookieManager = CookieManager.getInstance()
        val webview = WebView(this)
        webview.getSettings().setJavaScriptEnabled(true);
        cookieManager.setAcceptCookie(true)
        webview.loadUrl("google.com")
    }
    private fun builderUrl(base:Uri,query:String):Uri{
        var builder:Uri.Builder = Uri.Builder()
        builder.scheme(base.scheme)
        builder.authority(base.host)
        builder.path(base.path)
        builder.query(query)
        return builder.build()
    }
    companion object{
        private const val LOADING_PHASE = "test_url"
    }
}






