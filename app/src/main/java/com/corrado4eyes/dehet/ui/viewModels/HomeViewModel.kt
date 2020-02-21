package com.corrado4eyes.dehet.ui.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.corrado4eyes.dehet.delegates.ViewModelDelegate
import com.corrado4eyes.dehet.models.HistoryEntry
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

    private val dispatcher: DispatcherProvider by inject()

    private val coroutineUtil = CoroutineUtil(dispatcher)

    private val viewModelDelegate = ViewModelDelegate(yandexRepo, coroutineUtil)


    val editTextValue = MutableLiveData<String>()
    val resultHistoryEntry = MutableLiveData<HistoryEntry>()
    val historyList = MutableLiveData<List<HistoryEntry>>()

    init {
        editTextValue.value = ""
        resultHistoryEntry.value = null
        historyList.value = emptyList()
    }

    fun isAddEntryClickable(): Boolean {
        return resultHistoryEntry != null
    }

    private fun checkText(text: String): String {
        return viewModelDelegate.checkTextStructure(text)
    }

    suspend fun onSearchButtonClicked(text: String): HistoryEntry = coroutineUtil.doInBackground {
        val checkedText = checkText(text)
        return@doInBackground viewModelDelegate.getArticle(checkedText)
    }

    suspend fun onAddResultClicked(newEntry: HistoryEntry): List<HistoryEntry> = coroutineUtil.doInBackground {
        val oldList = historyList.value?.toMutableList()
            ?: mutableListOf()
        return@doInBackground viewModelDelegate.addArticle(newEntry, oldList)
    }
}