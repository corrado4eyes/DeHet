package com.corrado4eyes.dehet.data.network

import com.google.firebase.remoteconfig.FirebaseRemoteConfig

interface RemoteConfig {
    fun getRemoteConfig(): FirebaseRemoteConfig
    fun getApiKey(): String
}