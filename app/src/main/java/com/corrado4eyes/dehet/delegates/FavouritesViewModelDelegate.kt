package com.corrado4eyes.dehet.delegates

import com.corrado4eyes.dehet.models.HistoryEntry
import com.corrado4eyes.dehet.repos.DatabaseRepository
import com.corrado4eyes.dehet.util.CoroutineUtil

class FavouritesViewModelDelegate(private val databaseRepo: DatabaseRepository,
                                  private val coroutineUtil: CoroutineUtil) {

    suspend fun syncWithLocalDb(): List<HistoryEntry> = coroutineUtil.doInBackground {
        return@doInBackground databaseRepo.filter(true)
    }

    suspend fun onFavouriteButtonTapped(entry: HistoryEntry) {
        coroutineUtil.doInBackground {
            databaseRepo.upsert(entry)
        }
    }

    suspend fun onDeleteButtonTapped(entry: HistoryEntry) {
        coroutineUtil.doInBackground {
            databaseRepo.delete(entry)
        }
    }

}