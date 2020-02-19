package com.corrado4eyes.dehet.models

import com.google.gson.annotations.SerializedName

data class YandexResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("lang")
    val lang: String,
    @SerializedName("text")
    val text: List<String>
)