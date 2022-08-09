package com.example.hydrationapp2.data

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class RemoveUnusedRecordsWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {
    override fun doWork(): Result {
        val database = RecordRoomDatabase.getDatabase(applicationContext)
        database.recordDao().removeUnusedRecords()
        return Result.success()
    }
}