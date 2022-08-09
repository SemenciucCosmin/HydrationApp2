package com.example.hydrationapp2.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Record::class], version = 1, exportSchema = false)
abstract class RecordRoomDatabase : RoomDatabase() {

    abstract fun recordDao(): RecordDao

    companion object {
        @Volatile
        private var INSTANCE: RecordRoomDatabase? = null

        fun getDatabase(context: Context): RecordRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RecordRoomDatabase::class.java,
                    "record_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}