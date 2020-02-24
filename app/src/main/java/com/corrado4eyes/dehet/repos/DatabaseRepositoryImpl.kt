package com.corrado4eyes.dehet.repos

import com.corrado4eyes.dehet.models.HistoryEntry

class DatabaseRepositoryImpl : DatabaseRepository {
    override fun upsert(entry: HistoryEntry) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAll(): List<HistoryEntry> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun filter(isFavourite: Boolean): List<HistoryEntry> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}