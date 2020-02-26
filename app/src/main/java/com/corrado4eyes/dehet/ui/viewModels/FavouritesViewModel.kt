package com.corrado4eyes.dehet.ui.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.corrado4eyes.dehet.delegates.FavouritesViewModelDelegate
import com.corrado4eyes.dehet.models.HistoryEntry
import com.corrado4eyes.dehet.repos.DatabaseRepository
import com.corrado4eyes.dehet.util.CoroutineUtil
import com.corrado4eyes.dehet.util.DispatcherProvider
import org.koin.core.KoinComponent
import org.koin.core.inject

class FavouritesViewModel: ViewModel(), KoinComponent {

    private val databaseRepo by inject<DatabaseRepository>()

    private val dispatcherProvider by inject<DispatcherProvider>()

    private val coroutineUtil = CoroutineUtil(dispatcherProvider)

    private val favouritesViewModelDelegate =
        FavouritesViewModelDelegate(databaseRepo, coroutineUtil)

    val favouritesList = MutableLiveData<List<HistoryEntry>>()

    init {
        favouritesList.value = listOf(HistoryEntry("de", "hard-coded"))
    }


    suspend fun syncWithLocalDb(): List<HistoryEntry> = coroutineUtil.doInBackground {
        return@doInBackground favouritesViewModelDelegate.syncWithLocalDb()
    }

    suspend fun onFavouriteButtonTapped(entry: HistoryEntry) {
        coroutineUtil.doInBackground {
            entry.isFavourite = !entry.isFavourite
            favouritesViewModelDelegate.onFavouriteButtonTapped(entry)
        }
    }

    suspend fun onDeleteButtonTapped(entry: HistoryEntry) {
        coroutineUtil.doInBackground {
            favouritesViewModelDelegate.onDeleteButtonTapped(entry)
        }
    }

}