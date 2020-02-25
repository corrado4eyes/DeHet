package com.corrado4eyes.dehet.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "history")
data class HistoryEntry(
    @ColumnInfo(name="article")
    val article: String = "",
    @ColumnInfo(name="word")
    val word: String = "",
    @ColumnInfo(name="isFavourite")
    var isFavourite: Boolean = false) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var entryId: Long = 0

    override fun toString(): String {
        return "${article.capitalize()} $word"
    }
}