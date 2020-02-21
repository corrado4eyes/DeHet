package com.corrado4eyes.dehet.delegates

import com.corrado4eyes.dehet.models.HistoryEntry
import com.corrado4eyes.dehet.repos.YandexRepository
import com.corrado4eyes.dehet.util.CoroutineUtil
import org.koin.core.KoinComponent
import org.koin.core.inject

class ViewModelDelegate(private val yandexRepo: YandexRepository,
                        val coroutineUtil: CoroutineUtil): KoinComponent {
    companion object {
        private const val TAG = "ViewModelDelegate"
    }

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

    suspend fun getArticle(word: String): HistoryEntry = coroutineUtil.doInBackground {
        val fullText = "the $word"
        val response = yandexRepo.getTranslation("en-nl", fullText)
        val article = response.text.first().split(" ").first()
        val adverb = response.text.first().split(" ").last()
        return@doInBackground HistoryEntry(article, adverb)
    }

    suspend fun addArticle(newEntry: HistoryEntry,
                           oldList: MutableList<HistoryEntry>): List<HistoryEntry> =
        coroutineUtil.doInBackground {
        return@doInBackground oldList.plus(newEntry)
    }
}