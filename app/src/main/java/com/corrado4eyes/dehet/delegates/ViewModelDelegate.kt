package com.corrado4eyes.dehet.delegates

import com.corrado4eyes.dehet.models.HistoryEntry
import com.corrado4eyes.dehet.repos.YandexRepository
import com.corrado4eyes.dehet.util.doInBackground
import org.koin.core.KoinComponent
import org.koin.core.inject

class ViewModelDelegate: KoinComponent {
    companion object {
        private const val TAG = "ViewModelDelegate"
    }

    private val yandexRepo by inject<YandexRepository>()

    fun checkTextStructure(text: String): String {
        val splittedText = text.split(" ")
        return if(splittedText.size > 1) {
            // Return the single word if prefixes are added (like article or whatever)
            splittedText.last()
        } else {
            // Single word
            text
        }
    }

    suspend fun getArticle(word: String): HistoryEntry = doInBackground {
        val fullText = "the $word"
        val response = yandexRepo.getTranslation("en-nl", fullText)
        val article = response.text.first().split(" ").first()
        val adverb = response.text.first().split(" ").last()
        return@doInBackground HistoryEntry(article, adverb)
    }

    suspend fun addArticle(article: String, adverb: String, oldList: MutableList<HistoryEntry>): List<HistoryEntry> = doInBackground {
        val newEntry = HistoryEntry(article, adverb)
        return@doInBackground oldList.plus(newEntry)
    }
}