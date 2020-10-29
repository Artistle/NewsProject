package com.bigluckgame.sliopands.view
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bigluckgame.sliopands.R
import com.bigluckgame.sliopands.Utils.RecyclerViewAdapter
import com.bigluckgame.sliopands.ViewModel.LoadDataClass
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

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        //var manager: FragmentManager = supportFragmentManager
        var loadData: LoadDataClass
        recyclerView = this.findViewById<RecyclerView>(R.id.main_recycler)
        loadData = ViewModelProviders.of(this).get(LoadDataClass::class.java)
        loadData.loadData()
        loadData.simpleLiveData.observe(this, Observer{

            var adapter1 =  it?.let { it1 -> RecyclerViewAdapter(it1) }
            recyclerView.setHasFixedSize(true)
            val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(getApplicationContext())
            recyclerView.layoutManager = layoutManager
            recyclerView.setAdapter(adapter1)
        })
    }

//    fun oneS(){
//        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
//        OneSignal.startInit(this)
//            .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
//            .unsubscribeWhenNotificationsAreDisabled(true)
//            .init();
//    }
}






