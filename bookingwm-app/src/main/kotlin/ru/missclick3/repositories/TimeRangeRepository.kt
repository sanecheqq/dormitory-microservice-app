package ru.missclick3.repositories

import ru.missclick3.messages.dtos.TimeRangeDto

interface TimeRangeRepository {
    suspend fun bookTimeRange(
        address: String,
        wmNumber: Int,
        userId: String,
        userTg: String,
        startTime: String,
        endTime: String,
        date: String,
        withDrier: Boolean
    ) : Boolean

    suspend fun deleteBooking(
        id: String
    ) : Boolean
    suspend fun getTimeRangeById(id: String) : TimeRangeDto?

    suspend fun getTimeRange(
        address: String,
        wmNumber: Int,
        startTime: String,
        endTime: String,
        date: String
    ) : TimeRangeDto?

    suspend fun getTimeRangesForWm(
        address: String,
        wmNumber: Int,
        date: String
    ) : List<TimeRangeDto>

    suspend fun getTimeRangesForUser(userId: String, date: String) : List<TimeRangeDto>

    suspend fun getCurrentKeyUsers(address: String) : List<String>
}