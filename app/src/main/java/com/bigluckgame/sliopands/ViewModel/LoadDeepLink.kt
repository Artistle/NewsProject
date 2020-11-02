package com.bigluckgame.sliopands.ViewModel

import android.app.Activity
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.facebook.applinks.AppLinkData

class LoadDeepLink():ViewModel() {
    val simpleData = MutableLiveData<String>()
    fun loadDeep(context: Activity){
        AppLinkData.fetchDeferredAppLinkData(context) { appLinkData ->
            Log.i("TAG_DEEP","${appLinkData?.targetUri}")
            Log.d("Lifecycle","deepLink")
            var t = "hello"
            simpleData.setValue(t)
            //checkDeepLink(appLinkData?.targetUri.toString())
            if(appLinkData?.targetUri == null || appLinkData?.targetUri.toString() == "null"){
                //replacetoContent()
                //deepLinkFb = "hello"
                //checkDeepLink(appLinkData?.targetUri.toString())
            }else{
                //checkDeepLink(appLinkData?.targetUri.toString())
                //deepLinkFb = "hello"
            }
        }
    }
}