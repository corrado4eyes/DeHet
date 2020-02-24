package com.corrado4eyes.dehet.data.local.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.corrado4eyes.dehet.data.local.daos.HistoryDao
import com.corrado4eyes.dehet.models.HistoryEntry


@Database(entities = [HistoryEntry::class], version = 1, exportSchema = false)
abstract class HistoryDatabase: RoomDatabase() {
    abstract fun historyDao(): HistoryDao

    companion object {
        @Volatile private var INSTANCE: HistoryDatabase? = null

        fun getInstance(context: Context): HistoryDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context)
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                HistoryDatabase::class.java, "HistoryDb"
            ).build()
    }

}