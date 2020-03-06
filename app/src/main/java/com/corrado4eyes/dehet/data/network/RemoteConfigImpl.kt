package com.corrado4eyes.dehet.data.network

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
        remoteConfig.fetchAndActivate()
        return remoteConfig.getString("yandexApiKey")
    }
}