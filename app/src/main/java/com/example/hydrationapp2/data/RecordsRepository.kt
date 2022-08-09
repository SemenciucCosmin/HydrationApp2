package com.example.hydrationapp2.data

import android.content.Context
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.*
import org.threeten.bp.format.DateTimeFormatter
import java.util.*


class RecordsRepository(
    private val database: RecordRoomDatabase,
    private val settingsDataSource: SettingsDataSource
) {

    fun getRecords(): Flow<List<Record>> = database.recordDao().getRecords()

    fun getCurrentRecord(): Flow<Record> {
        val formatter = DateTimeFormatter.ofPattern(DATE_PATTERN)
        val date = LocalDate.now().format(formatter)
        return database.recordDao().getRecord(date)
    }

    suspend fun addNewRecord() {
        val formatter = DateTimeFormatter.ofPattern(DATE_PATTERN, Locale.ENGLISH)
        val millis = System.currentTimeMillis()
        val instant = Instant.ofEpochMilli(millis)
        val fullDate = LocalDateTime.ofInstant(instant, ZoneId.of("Europe/London"))
        val date = formatter.format(fullDate)
        val record = Record(date, millis, DEFAULT_INTAKE)
        database.recordDao().insert(record)
    }

    suspend fun addIntake(intake: Int) {
        val formatter = DateTimeFormatter.ofPattern(DATE_PATTERN)
        val date = LocalDate.now().format(formatter)
        val millis = System.currentTimeMillis()
        val record = Record(date, millis, intake)
        database.recordDao().update(record)
    }

    fun getSettingsPreferences(): Flow<SettingsPreferences> = settingsDataSource.measurementUnitFlow

    suspend fun saveMeasurementUnit(measurementUnit: String, context: Context) {
        settingsDataSource.saveMeasurementUnitToPreferenceStore(measurementUnit, context)
    }

    suspend fun saveGoalSize(goalSize: Int, context: Context) {
        settingsDataSource.saveGoalToPreferenceStore(goalSize, context)
    }

    suspend fun saveContainer1Size(container1Size: Int, context: Context) {
        settingsDataSource.saveContainer1ToPreferenceStore(container1Size, context)
    }

    suspend fun saveContainer2Size(container2Size: Int, context: Context) {
        settingsDataSource.saveContainer2ToPreferenceStore(container2Size, context)
    }

    suspend fun saveContainer3Size(container3Size: Int, context: Context) {
        settingsDataSource.saveContainer3ToPreferenceStore(container3Size, context)
    }
}