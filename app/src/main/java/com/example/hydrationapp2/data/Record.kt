package com.example.hydrationapp2.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "record")
data class Record(
    @PrimaryKey
    @ColumnInfo(name = "date")
    val date: String,

    @ColumnInfo(name = "millis")
    val millis: Long,

    @ColumnInfo(name = "intake")
    val intake: Int
)
