package com.corrado4eyes.dehet.models

data class HistoryEntry(
    val article: String = "",
    val adverb: String = "",
    val isFavourite: Boolean = false) {
    override fun toString(): String {
        return "$article $adverb"
    }
}