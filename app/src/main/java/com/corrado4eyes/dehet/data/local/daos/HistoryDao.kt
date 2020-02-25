package com.corrado4eyes.dehet.data.local.daos

import androidx.room.*
import com.corrado4eyes.dehet.models.HistoryEntry

@Dao
interface HistoryDao {

    /**
     * Get the whole history.
     */
    @Query("Select * from history")
    fun getHistory(): List<HistoryEntry>

    /**
     * Insert new entry or replace oldOne with newOne.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertEntry(entry: HistoryEntry): Long

    /**
     * Delete the given entry from the db
     */
    @Delete
    fun deleteEntry(entry: HistoryEntry)

    /**
     * Filter the results by the favourite field.
     */
    @Query("Select * from history where isFavourite = :isFavourite")
    fun filterByFavourite(isFavourite: Boolean = false): List<HistoryEntry>

    /**
     * Return the number of stored rows.
     */
    @Query("Select COUNT(id) from history")
    fun count(): Int
}