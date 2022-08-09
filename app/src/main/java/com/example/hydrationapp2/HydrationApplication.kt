package com.example.hydrationapp2

import android.app.Application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.hydrationapp2.data.REMOVE_UNUSED_RECORDS_WORK
import com.example.hydrationapp2.data.RemoveUnusedRecordsWorker
import com.example.hydrationapp2.data.WORK_REPEAT_INTERVAL
import com.jakewharton.threetenabp.AndroidThreeTen
import java.util.concurrent.TimeUnit

class HydrationApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)

        val periodicWorkRequest = PeriodicWorkRequest.Builder(RemoveUnusedRecordsWorker::class.java, WORK_REPEAT_INTERVAL, TimeUnit.MINUTES).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            REMOVE_UNUSED_RECORDS_WORK,
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest
        )
    }
}