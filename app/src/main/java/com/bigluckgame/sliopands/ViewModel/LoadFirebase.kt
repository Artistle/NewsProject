package com.bigluckgame.sliopands.ViewModel

import android.app.Activity
import android.util.Base64
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bigluckgame.sliopands.R
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

class LoadFirebase:ViewModel() {
    val simpleLiveData = MutableLiveData<String>()
    var remoteConfig = Firebase.remoteConfig
    var configSettings = remoteConfigSettings {
        minimumFetchIntervalInSeconds = 50
    }

    fun load(context: Activity){


        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.default_resource_config)
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(context) { task ->
            if (task.isSuccessful) {
                val updated = task.result
                Log.i("TAG", "Config params updated: $updated")
            } else {
                Log.i("TAG", "Config params not_updated: ${task.exception}")
            }

        }

        var url = remoteConfig[LOADING_PHASE].asString()
        simpleLiveData.setValue(decoderUrl(url))
        Log.i("TAGG","$remoteConfig[LOADING_PHASE]")
    }
    private fun decoderUrl(coderUrl:String):String{
        var byteUrl = Base64.decode(coderUrl,1)
        var mainUrl = String(byteUrl)
        return mainUrl
    }
    companion object{
        private const val LOADING_PHASE = "LOADING_PHASE"
    }
}