package com.corrado4eyes.dehet.data.network

import com.corrado4eyes.dehet.models.YandexResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface YandexApi {
    @GET("translate")
    suspend fun getTranslation(@Query("lang") lang: String,
                               @Query("text") text: String): YandexResponse
}