package com.example.foodcourt.View

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodcourt.R
import com.example.foodcourt.Utils.RecyclerViewAdapter
import com.example.foodcourt.ViewModel.LoadDataClass
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging

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
    private lateinit var recyclerView: RecyclerView
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var manager: FragmentManager = supportFragmentManager
        var loadData:LoadDataClass
        recyclerView = this.findViewById<RecyclerView>(R.id.main_recycler)
        loadData = ViewModelProviders.of(this).get(LoadDataClass::class.java)
        loadData.loadData()
        loadData.simpleLiveData.observe(this,Observer{

            var adapter1 =  it?.let { it1 -> RecyclerViewAdapter(it1,manager) }
            recyclerView.setHasFixedSize(true)
            val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(getApplicationContext())
            recyclerView.layoutManager = layoutManager
            recyclerView.setAdapter(adapter1)
        })

        /*не знал как и куда вставить webView и поэтому решил просто сделать кнопку перехода*/
        var intent = Intent(this,WebViewActivity::class.java)
        var fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                startActivity(intent)
            }

        })

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


    }
}






