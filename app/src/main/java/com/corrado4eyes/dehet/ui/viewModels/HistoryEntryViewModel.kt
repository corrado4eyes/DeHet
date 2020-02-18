package com.corrado4eyes.dehet.ui.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HistoryEntryViewModel(private val _adverb: String, private val _article: String): ViewModel() {
    val article = MutableLiveData<String>()
    val adverb = MutableLiveData<String>()

    init {
        article.value = _adverb
        adverb.value = _article
    }
}