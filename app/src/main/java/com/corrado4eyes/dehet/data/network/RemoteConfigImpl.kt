package com.corrado4eyes.dehet.data.network

import android.util.Log
import com.corrado4eyes.dehet.R
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

class RemoteConfigImpl : RemoteConfig {

    override fun getRemoteConfig(): FirebaseRemoteConfig {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(3600)
            .build()
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config)
        return remoteConfig
    }

    override fun getApiKey(): String {
        val remoteConfig = getRemoteConfig()
        remoteConfig.fetchAndActivate().addOnCompleteListener {
            if(it.isSuccessful) {
                it.result
                Log.d("RemoteConfigImpl", "Successful ${it.result}")


            } else {
                Log.d("RemoteConfigImpl", "Failed ${it.result}")
            }
        }
        return remoteConfig.getString("yandexApiKey")
    }
}