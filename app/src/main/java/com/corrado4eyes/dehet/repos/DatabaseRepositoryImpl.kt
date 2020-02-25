package com.corrado4eyes.dehet.repos

import com.corrado4eyes.dehet.data.local.daos.HistoryDao
import com.corrado4eyes.dehet.models.HistoryEntry

class DatabaseRepositoryImpl(private val historyDao: HistoryDao) : DatabaseRepository {
    override fun upsert(entry: HistoryEntry) {
        historyDao.upsertEntry(entry)
    }

    override fun getAll(): List<HistoryEntry> {
        return historyDao.getHistory()
    }

    override fun filter(isFavourite: Boolean): List<HistoryEntry> {
        return historyDao.filterByFavourite(isFavourite)
    }

    override fun count(): Int {
        return historyDao.count()
    }
}