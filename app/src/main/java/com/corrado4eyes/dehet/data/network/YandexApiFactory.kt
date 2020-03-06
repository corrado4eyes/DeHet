package com.corrado4eyes.dehet.data.network

import com.corrado4eyes.dehet.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object YandexApiFactory: KoinComponent {

    private val remoteConfig: RemoteConfig by inject()

    private const val baseUrl = "https://translate.yandex.net/api/v1.5/tr.json/"
    private const val apiKey = BuildConfig.API_KEY

    fun create(): YandexApi {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .client(createOkHttpClient())
            .build()
        return retrofit.create(YandexApi::class.java)
    }

    private fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(createInterceptor())
            .addInterceptor(createLogging())
            .build()
    }

    private fun createInterceptor(): Interceptor {
        return Interceptor { chain ->
            val url = chain.request()
                .url()
                .newBuilder()
                .addQueryParameter("key", apiKey)
                .build()

            val request = chain.request()
                .newBuilder()
                .url(url)
                .build()
            return@Interceptor chain.proceed(request)
        }
    }

    private fun createLogging(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
    }
}