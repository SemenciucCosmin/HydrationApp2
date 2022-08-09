package com.example.hydrationapp2.data

class SettingsPreferences(
    val measurementUnit: String = DEFAULT_UNIT,
    val goal: Int = DEFAULT_GOAL,
    val container1: Int = DEFAULT_CONTAINER1_SIZE,
    val container2: Int = DEFAULT_CONTAINER2_SIZE,
    val container3: Int = DEFAULT_CONTAINER3_SIZE
)