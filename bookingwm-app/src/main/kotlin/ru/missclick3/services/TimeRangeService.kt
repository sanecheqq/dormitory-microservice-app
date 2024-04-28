package ru.missclick3.services

import ru.missclick3.messages.dtos.TimeRangeDto
import ru.missclick3.messages.requests.*

interface TimeRangeService {
    suspend fun bookTimeRange(
        request: BookTimeRangeRequest
    ) : Boolean

    suspend fun deleteBooking(
        request: GetTimeRangeRequest
    ) : Boolean

    suspend fun getTimeRange(
        request: GetTimeRangeRequest
    ) : TimeRangeDto?

    suspend fun getTimeRangesForWm(
        request: GetTimeRangesForWmRequest
    ) : List<TimeRangeDto>

    suspend fun getTimeRangesForUser(request: GetTimeRangesForUserRequest) : List<TimeRangeDto>

    suspend fun getCurrentKeyUsers(request: GetWMsForDormitoryRequest) : List<String>

    suspend fun getAmountOfTimeRangersForUser(request: GetTimeRangesForUserRequest) : Int
}