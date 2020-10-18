package com.example.foodcourt.Model.NetConfigure

import com.example.foodcourt.Model.ModelNews
import retrofit2.Call
import retrofit2.http.GET

interface NetApi {
    @GET("all.json?api-key=IV83NfimcfZqZ8Et1QEnoYxaRAP9lhWh")
    fun getList(): Call<ModelNews>
}