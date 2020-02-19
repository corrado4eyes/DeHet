package com.corrado4eyes.dehet.repos

import com.corrado4eyes.dehet.models.YandexResponse

interface YandexRepository {
   suspend fun getTranslation(lang: String, text: String): YandexResponse
}