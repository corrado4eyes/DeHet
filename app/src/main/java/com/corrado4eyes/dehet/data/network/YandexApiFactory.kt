package com.corrado4eyes.dehet.data.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object YandexApiFactory {

    private const val baseUrl = "https://translate.yandex.net/api/v1.5/tr.json/"
    private const val apiKey =
        "trnsl.1.1.20200218T154614Z.7a67a4387968adde.189f43754868f7a15b9718613a70d46d727f3f17"

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