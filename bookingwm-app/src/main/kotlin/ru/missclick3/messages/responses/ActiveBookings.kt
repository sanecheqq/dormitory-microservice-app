package ru.missclick3.messages.responses

import kotlinx.serialization.Serializable
import ru.missclick3.messages.dtos.TimeRangeDto

@Serializable
data class ActiveBookings(
    val todayBookings: List<TimeRangeDto>,
    val tomorrowBookings: List<TimeRangeDto>
)
