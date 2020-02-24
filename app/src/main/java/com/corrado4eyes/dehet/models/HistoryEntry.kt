package com.corrado4eyes.dehet.models

data class HistoryEntry(
    val article: String = "",
    val word: String = "",
    var isFavourite: Boolean = false) {
    override fun toString(): String {
        return "${article.capitalize()} $word"
    }
}