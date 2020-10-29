package com.bigluckgame.sliopands.Model.NetConfigure

import com.bigluckgame.sliopands.Model.ModelNews
import retrofit2.Call
import retrofit2.http.GET

interface NetApi {
    @GET("all.json?api-key=IV83NfimcfZqZ8Et1QEnoYxaRAP9lhWh")
    fun getList(): Call<ModelNews>
}