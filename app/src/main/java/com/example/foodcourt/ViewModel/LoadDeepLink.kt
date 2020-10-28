package com.example.foodcourt.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodcourt.Model.ModelNews

class LoadDeepLink:ViewModel(),DeepLink {
    val simpleLiveData = MutableLiveData<String>()
    override fun setValue(url: String) {

    }

    fun load(){
        object : DeepLink{
            override fun setValue(url: String) {
                simpleLiveData.setValue(url)
                var t="f"
                var e="f"
            }

        }
    }

}