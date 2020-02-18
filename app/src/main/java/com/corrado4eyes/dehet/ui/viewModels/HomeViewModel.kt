package com.corrado4eyes.dehet.ui.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.corrado4eyes.dehet.delegates.ViewModelDelegate
import com.corrado4eyes.dehet.models.HistoryEntry
import com.corrado4eyes.dehet.util.doInBackground

class HomeViewModel: ViewModel() {

    companion object {
        private const val TAG = "HomeViewModel"
    }
    private val viewModelDelegate = ViewModelDelegate()


    val editTextValue = MutableLiveData<String>()
    val resultLabel = MutableLiveData<HistoryEntry>()
    val historyList = MutableLiveData<List<HistoryEntry>>()

    init {
        editTextValue.value = ""
        resultLabel.value = HistoryEntry()
        historyList.value = emptyList()
    }

    suspend fun onSearchButtonClicked(): HistoryEntry = doInBackground {
        return@doInBackground viewModelDelegate.getArticle(editTextValue.value!!)
    }

    suspend fun onAddResultClicked(): List<HistoryEntry> = doInBackground {
        return@doInBackground viewModelDelegate.addArticle(resultLabel.value!!.article,
            resultLabel.value!!.adverb,
            historyList.value!!.toMutableList())
    }
}