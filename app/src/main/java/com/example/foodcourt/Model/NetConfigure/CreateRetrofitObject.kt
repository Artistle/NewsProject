package com.example.foodcourt.Model.NetConfigure

import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


//package NetConfigure для хранения классов,объектов,интерфейсов,служащих непосредственно для конфигурации подключения
//глобальный объект ретрофита,возвращающий собранный и готовый к работе Retrofit
object CreateRetrofitObject {
         private val retrofit =  Retrofit.Builder()
            .baseUrl("https://api.nytimes.com/svc/news/v3/content/all/")
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        fun<T> buildService(service: Class<T>): T{
            return retrofit.create(service)
        }


}