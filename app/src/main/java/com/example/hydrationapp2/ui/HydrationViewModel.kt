package com.example.hydrationapp2.ui

import android.app.Application
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.hydrationapp2.HydrationApplication
import com.example.hydrationapp2.R
import com.example.hydrationapp2.data.ContainerType
import com.example.hydrationapp2.data.DATE_DIVIDER
import com.example.hydrationapp2.data.DATE_PATTERN
import com.example.hydrationapp2.data.DEFAULT_GOAL
import com.example.hydrationapp2.data.DEFAULT_INTAKE
import com.example.hydrationapp2.data.DEFAULT_UNIT
import com.example.hydrationapp2.data.FIRST
import com.example.hydrationapp2.data.FullRecord
import com.example.hydrationapp2.data.HISTORY_RECORDS_NR
import com.example.hydrationapp2.data.MAX_RECORD_NR
import com.example.hydrationapp2.data.Record
import com.example.hydrationapp2.data.RecordRoomDatabase
import com.example.hydrationapp2.data.RecordsRepository
import com.example.hydrationapp2.data.SettingsDataSource
import com.example.hydrationapp2.data.SettingsPreferences
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.launch

class HydrationViewModel(application: Application) : AndroidViewModel(application) {
    private val recordsRepository = RecordsRepository(
        RecordRoomDatabase.getDatabase(application),
        SettingsDataSource(application)
    )

    private var intake = DEFAULT_INTAKE
    private var settings = SettingsPreferences()

    fun getFullRecords(goal: Int, measurementUnit: String): LiveData<List<FullRecord>> {
        return Transformations.map(recordsRepository.getRecords().asLiveData()) { list ->
            list.map { record ->
                FullRecord(record.date, record.intake, measurementUnit, record.millis, goal)
            }
        }
    }

    fun getCurrentRecord(): LiveData<Record> {
        viewModelScope.launch {
            recordsRepository.getCurrentRecord().collect {
                intake = it.intake
            }
        }
        return recordsRepository.getCurrentRecord().asLiveData()
    }

    fun addNewRecord() = viewModelScope.launch { recordsRepository.addNewRecord() }

    fun getSettings(): SettingsPreferences {
        return settings
    }

    fun getIntake(): Int = intake

    fun getSettingsPreferences(): LiveData<SettingsPreferences> {
        viewModelScope.launch {
            recordsRepository.getSettingsPreferences().collect {
                settings = it
            }
        }
        return recordsRepository.getSettingsPreferences().asLiveData()
    }

    fun addIntake(containerType: Int) {
        viewModelScope.launch {
            when (ContainerType.getByContainerType(containerType)) {
                ContainerType.CONTAINER_1 -> recordsRepository.addIntake(settings.container1 + intake)
                ContainerType.CONTAINER_2 -> recordsRepository.addIntake(settings.container2 + intake)
                ContainerType.CONTAINER_3 -> recordsRepository.addIntake(settings.container3 + intake)
                ContainerType.DAILY_GOAL -> {}
            }
        }
    }

    private fun getStartAndEndMillis(records: List<FullRecord>): Pair<Long, Long> {
        var startRecordMillis = records[FIRST].millis
        val endRecordMillis =
            if (records.size > 1) records[records.lastIndex].millis else records[FIRST].millis

        val daysDifference = endRecordMillis - startRecordMillis
        if ((daysDifference / DATE_DIVIDER) < HISTORY_RECORDS_NR) {
            startRecordMillis =
                (System.currentTimeMillis() / DATE_DIVIDER - HISTORY_RECORDS_NR) * DATE_DIVIDER
        }

        return Pair(startRecordMillis, endRecordMillis)
    }

    fun getBarChartRecords(databaseRecords: List<FullRecord>): List<FullRecord> {
        val completeRecords = mutableListOf<FullRecord>()

        val millis = getStartAndEndMillis(databaseRecords)
        var currentMillis = millis.first

        for (id in 0 until MAX_RECORD_NR) {
            val record = getNewOrExistingFullRecord(databaseRecords, currentMillis)
            completeRecords.add(record)
            currentMillis += DATE_DIVIDER
        }

        return completeRecords
    }

    private fun getNewOrExistingFullRecord(
        records: List<FullRecord>,
        currentMillis: Long
    ): FullRecord {
        val formatter = SimpleDateFormat(DATE_PATTERN, Locale.ENGLISH)
        val currentDate = formatter.format(Date(currentMillis))

        records.forEach {
            if (it.date == currentDate) {
                return it
            }
        }
        return FullRecord(currentDate, DEFAULT_INTAKE, DEFAULT_UNIT, currentMillis, DEFAULT_GOAL)
    }

    fun createBarDataSet(
        id: Int,
        intake: Int,
        goal: Int,
        context: Context
    ): BarDataSet {
        val barDataSet = BarDataSet(listOf(BarEntry(id.toFloat(), intake.toFloat())), "")
        barDataSet.setDrawValues(false)

        if (intake < goal) {
            barDataSet.color = ContextCompat.getColor(context, R.color.yellow)
        } else {
            barDataSet.color = ContextCompat.getColor(context, R.color.green)
        }

        barDataSet.barShadowColor = ContextCompat.getColor(context, R.color.gray_2)
        return barDataSet
    }

    fun getPercentage(intake: Int): Int {
        return if (intake != 0) (intake * 100) / settings.goal else intake
    }

    fun saveMeasurementUnit(measurementUnit: String, context: Context) {
        viewModelScope.launch { recordsRepository.saveMeasurementUnit(measurementUnit, context) }
    }

    fun saveGoalSize(goalSize: Int, context: Context) {
        viewModelScope.launch { recordsRepository.saveGoalSize(goalSize, context) }
    }

    fun saveContainer1Size(container1Size: Int, context: Context) {
        viewModelScope.launch { recordsRepository.saveContainer1Size(container1Size, context) }
    }

    fun saveContainer2Size(container2Size: Int, context: Context) {
        viewModelScope.launch { recordsRepository.saveContainer2Size(container2Size, context) }
    }

    fun saveContainer3Size(container3Size: Int, context: Context) {
        viewModelScope.launch { recordsRepository.saveContainer3Size(container3Size, context) }
    }

    class HydrationViewModelFactory(val app: HydrationApplication) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HydrationViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HydrationViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct view model")
        }
    }
}

