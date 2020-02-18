package com.corrado4eyes.dehet.ui.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.corrado4eyes.dehet.models.HistoryEntry

class HistoryEntryViewModel(private val _entry: HistoryEntry): ViewModel() {
    val entry = MutableLiveData<HistoryEntry>()

    init {
        entry.value = _entry
    }
}