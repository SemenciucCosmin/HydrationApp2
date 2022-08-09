package com.example.hydrationapp2.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = SETTINGS_PREFERENCE_NAME)

class SettingsDataSource(context: Context) {
    private val MEASUREMENT_UNIT = stringPreferencesKey("measurement_unit")
    private val GOAL = intPreferencesKey("goal")
    private val CONTAINER_1 = intPreferencesKey("container_1")
    private val CONTAINER_2 = intPreferencesKey("container_2")
    private val CONTAINER_3 = intPreferencesKey("container_3")

    val measurementUnitFlow: Flow<SettingsPreferences> = context.dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            val measurementUnit = preferences[MEASUREMENT_UNIT] ?: "ml"
            val goal = preferences[GOAL] ?: 2000
            val container1 = preferences[CONTAINER_1] ?: 100
            val container2 = preferences[CONTAINER_2] ?: 250
            val container3 = preferences[CONTAINER_3] ?: 750

            SettingsPreferences(measurementUnit, goal, container1, container2, container3)
        }

    suspend fun saveMeasurementUnitToPreferenceStore(measurementUnit: String, context: Context) {
        context.dataStore.edit { preferences ->
            preferences[MEASUREMENT_UNIT] = measurementUnit
        }
    }

    suspend fun saveGoalToPreferenceStore(goal: Int, context: Context) {
        context.dataStore.edit { preferences ->
            preferences[GOAL] = goal
        }
    }

    suspend fun saveContainer1ToPreferenceStore(container1: Int, context: Context) {
        context.dataStore.edit { preferences ->
            preferences[CONTAINER_1] = container1
        }
    }

    suspend fun saveContainer2ToPreferenceStore(container2: Int, context: Context) {
        context.dataStore.edit { preferences ->
            preferences[CONTAINER_2] = container2
        }
    }

    suspend fun saveContainer3ToPreferenceStore(container3: Int, context: Context) {
        context.dataStore.edit { preferences ->
            preferences[CONTAINER_3] = container3
        }
    }
}