package com.corrado4eyes.dehet.repos

import com.corrado4eyes.dehet.models.HistoryEntry

interface DatabaseRepository {
    fun upsert(entry: HistoryEntry)

    fun getAll(): List<HistoryEntry>

    fun filter(isFavourite: Boolean): List<HistoryEntry>

    fun count(): Int
}