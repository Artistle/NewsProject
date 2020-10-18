package com.example.foodcourt.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodcourt.Model.ModelNews
import com.example.foodcourt.Model.NetConfigure.NetApi
import com.example.foodcourt.Model.NetConfigure.CreateRetrofitObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoadDataClass : ViewModel() {

    // LiveData добавил для передачи данных в Activity,и для работы с жизненным циклом при повороте экране
    val simpleLiveData = MutableLiveData<ModelNews>()

    /*
    retrofit работает в фоновом потоке,поэтому не вижу смысла в этой ситуации создавать отдельный поток,или использовать rxJava2
     */
    fun loadData(){

        val request = CreateRetrofitObject.buildService(NetApi::class.java)
        val call = request.getList()
        call.enqueue(object : Callback<ModelNews> {
            override fun onFailure(call: Call<ModelNews>, t: Throwable) {
                Log.i("Failure","${t.localizedMessage}")
            }

            override fun onResponse(call: Call<ModelNews>, response: Response<ModelNews>) {
                Log.i("succes","${response.body()}")
                simpleLiveData.setValue(response.body())
            }

        })
    }




}