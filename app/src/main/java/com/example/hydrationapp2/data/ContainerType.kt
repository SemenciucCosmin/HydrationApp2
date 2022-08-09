package com.example.hydrationapp2.data

enum class ContainerType(val containerType: Int) {
    DAILY_GOAL(0),
    CONTAINER_1(1),
    CONTAINER_2(2),
    CONTAINER_3(3);

    companion object {
        fun getByContainerType(containerType: Int) =
            values().firstOrNull { it.containerType == containerType } ?: DAILY_GOAL
    }
}