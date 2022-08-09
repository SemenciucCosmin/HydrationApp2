package com.example.hydrationapp2.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordDao {

    @Query("SELECT * from record ORDER BY millis DESC LIMIT 30")
    fun getRecords(): Flow<List<Record>>

    @Query("SELECT * from record WHERE date = :date")
    fun getRecord(date: String): Flow<Record>

    @Query("DELETE from record WHERE date NOT IN (SELECT date from record ORDER BY millis DESC LIMIT 30)")
    fun removeUnusedRecords()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(record: Record)

    @Update
    suspend fun update(record: Record)

    @Delete
    suspend fun delete(record: Record)
}