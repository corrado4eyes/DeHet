package com.corrado4eyes.dehet.ui.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.corrado4eyes.dehet.delegates.ViewModelDelegate
import com.corrado4eyes.dehet.util.doInBackground

class HomeViewModel: ViewModel() {

    companion object {
        private const val TAG = "HomeViewModel"
    }
    private val viewModelDelegate = ViewModelDelegate()


    val editTextValue = MutableLiveData<String>()
    val resultLabel = MutableLiveData<String>()

    init {
        editTextValue.value = ""
        resultLabel.value = ""
    }

    suspend fun onSearchButtonClicked() = doInBackground {
        return@doInBackground viewModelDelegate.getArticle(editTextValue.value!!)
    }
}