package com.corrado4eyes.dehet.delegates

import com.corrado4eyes.dehet.models.HistoryEntry
import com.corrado4eyes.dehet.repos.DatabaseRepository
import com.corrado4eyes.dehet.repos.YandexRepository
import com.corrado4eyes.dehet.util.CoroutineUtil
import org.koin.core.KoinComponent

open class ViewModelDelegate(private val yandexRepo: YandexRepository,
                        private val databaseRepo: DatabaseRepository,
                        private val coroutineUtil: CoroutineUtil): KoinComponent {
    companion object {
        private const val TAG = "ViewModelDelegate"
    }

    private fun cleanText(text: String): String {
        return text.replace("\n", "")
    }

    fun checkTextStructure(text: String): String {
        val splittedText = cleanText(text).split(" ")
        return splittedText.filter { s ->
            s.toLowerCase() != "the"
        }.joinToString(" ")
    }

    suspend fun getArticle(word: String): HistoryEntry = coroutineUtil.doInBackground {
        val fullText = "the ${word.trim()}"
        val response = yandexRepo.getTranslation("en-nl", fullText)
        val article = response.text.first().split(" ").first()
        val adverb = response.text.first().split(" ").last()
        return@doInBackground HistoryEntry(article, adverb)
    }

    suspend fun upsertEntry(entry: HistoryEntry) {
        coroutineUtil.doInBackground {
            databaseRepo.upsert(entry)
        }
    }

    suspend fun getHistory(): List<HistoryEntry> = coroutineUtil.doInBackground {
        return@doInBackground databaseRepo.getAll()
    }

    suspend fun deleteEntry(entry: HistoryEntry) = coroutineUtil.doInBackground {
        databaseRepo.delete(entry)
    }

    suspend fun filterEntries(isFilterFavourite: Boolean): List<HistoryEntry> =
        coroutineUtil.doInBackground {
            return@doInBackground databaseRepo.filter(isFilterFavourite)
        }

    suspend fun countRows(): Int =  coroutineUtil.doInBackground { databaseRepo.count() }
}