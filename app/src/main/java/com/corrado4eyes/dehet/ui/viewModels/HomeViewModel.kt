package com.corrado4eyes.dehet.ui.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.Database
import com.corrado4eyes.dehet.delegates.ViewModelDelegate
import com.corrado4eyes.dehet.models.HistoryEntry
import com.corrado4eyes.dehet.repos.DatabaseRepository
import com.corrado4eyes.dehet.repos.YandexRepository
import com.corrado4eyes.dehet.util.CoroutineUtil
import com.corrado4eyes.dehet.util.DispatcherProvider
import org.koin.core.KoinComponent
import org.koin.core.inject

class HomeViewModel: ViewModel(), KoinComponent {

    companion object {
        private const val TAG = "HomeViewModel"
    }

    private val yandexRepo: YandexRepository by inject()

    private val databaseRepo: DatabaseRepository by inject()

    private val dispatcher: DispatcherProvider by inject()

    private val coroutineUtil = CoroutineUtil(dispatcher)

    private val viewModelDelegate = ViewModelDelegate(yandexRepo, databaseRepo,coroutineUtil)


    val editTextValue = MutableLiveData<String>()
    val resultHistoryEntry = MutableLiveData<HistoryEntry>()
    val historyList = MutableLiveData<List<HistoryEntry>>()

    init {
        editTextValue.value = ""
        resultHistoryEntry.value = null
        historyList.value = emptyList()
    }

    suspend fun onResumeHistoryState(): List<HistoryEntry> = coroutineUtil.doInBackground {
        return@doInBackground viewModelDelegate.getHistory()
    }

    private fun checkText(text: String): String {
        return viewModelDelegate.checkTextStructure(text)
    }

    fun isAddEntryClickable(): Boolean {
        return resultHistoryEntry != null
    }

    suspend fun onFavouriteButtonClicked(position: Int): List<HistoryEntry> =
        coroutineUtil.doInBackground {
            val entry = historyList.value!![position]
            entry.isFavourite = !entry.isFavourite
            viewModelDelegate.upsertEntry(entry)
            return@doInBackground viewModelDelegate.getHistory()
    }

    suspend fun onSearchButtonClicked(text: String): HistoryEntry = coroutineUtil.doInBackground {
        val checkedText = checkText(text)
        return@doInBackground viewModelDelegate.getArticle(checkedText)
    }

    suspend fun onAddResultClicked(newEntry: HistoryEntry): List<HistoryEntry> =
        coroutineUtil.doInBackground {
            viewModelDelegate.upsertEntry(newEntry)
            return@doInBackground viewModelDelegate.getHistory()
    }
}