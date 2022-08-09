package com.example.hydrationapp2.data

enum class UnitType(val unitType: String) {
    MILLILITERS("ml"),
    OUNCES("oz");

    companion object {
        fun getByUnitType(unitType: String) =
            values().firstOrNull { it.unitType == unitType } ?: MILLILITERS
    }
}