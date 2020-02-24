package com.corrado4eyes.dehet.ui.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.corrado4eyes.dehet.R
import com.corrado4eyes.dehet.models.HistoryEntry

class HistoryEntryViewModel(_entry: HistoryEntry): ViewModel() {

    companion object {
        private const val TAG = "HistoryEntryViewModel"
    }

    val entry = MutableLiveData<HistoryEntry>()
    val isFavouriteImage = MutableLiveData<@androidx.annotation.DrawableRes Int>()

    init {
        entry.value = _entry
        isFavouriteImage.value = R.drawable.empty_bookmark
    }

    fun onToggleFavouriteButton() {
        val isFavourite = !entry.value?.isFavourite!!
        entry.value!!.isFavourite = isFavourite
        Log.d(TAG, entry.value!!.isFavourite.toString())

        if(isFavourite) {
            isFavouriteImage.value = R.drawable.bookmark
        } else {
            isFavouriteImage.value = R.drawable.empty_bookmark
        }
    }
}