package ru.missclick3.services

import ru.missclick3.messages.dtos.TimeRangeDto
import ru.missclick3.messages.requests.*
import ru.missclick3.messages.responses.ActiveBookings

interface TimeRangeService {
    suspend fun bookTimeRange(
        request: BookTimeRangeRequest
    ) : Boolean

    suspend fun deleteBooking(
        request: GetTimeRangeById
    ) : Boolean

    suspend fun getTimeRange(
        request: GetTimeRangeRequest
    ) : TimeRangeDto?

    suspend fun getTimeRangesForWm(
        request: GetTimeRangesForWmRequest
    ) : List<TimeRangeDto>
    suspend fun getTimeRangeById(request: GetTimeRangeById) : TimeRangeDto?
    suspend fun getTimeRangesForUser(request: GetTimeRangesForUserRequest) : List<TimeRangeDto>

    suspend fun getCurrentKeyUsers(request: GetWMsForDormitoryRequest) : List<String>

    suspend fun getActiveBookingsForUser(request: UserIDRequest) : ActiveBookings
}