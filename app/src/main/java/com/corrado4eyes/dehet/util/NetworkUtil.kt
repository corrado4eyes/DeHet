package com.corrado4eyes.dehet.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest

class NetworkUtil() {
    companion object {
        private val connectivityCallback = NetworkCallbackImpl.connectivityCallBack

        fun registerNetworkCallback(context: Context) {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connectivityManager.registerNetworkCallback(NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build(), connectivityCallback)
        }

        fun unregisterNetworkCallback(context: Context) {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connectivityManager.unregisterNetworkCallback(connectivityCallback)
        }

        fun isDeviceConnected(): Boolean {
            return NetworkCallbackImpl.isConnected
        }
    }
}