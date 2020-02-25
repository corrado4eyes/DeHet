package com.corrado4eyes.dehet.repos

import com.corrado4eyes.dehet.data.network.YandexApi
import com.corrado4eyes.dehet.models.YandexResponse

class YandexRepositoryImpl(private val apiService: YandexApi): YandexRepository {

    override suspend fun getTranslation(lang: String, text: String): YandexResponse {
        return apiService.getTranslation("en-nl", text)
    }

}