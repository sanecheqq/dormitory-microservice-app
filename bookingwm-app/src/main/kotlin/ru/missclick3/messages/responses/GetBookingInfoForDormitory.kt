package ru.missclick3.messages.responses

import com.google.common.math.IntMath
import kotlinx.serialization.Serializable
import ru.missclick3.messages.dtos.TimeRangeDto

@Serializable
data class GetBookingInfoForDormitory(
    val date: String,
    val wmNumber: Int,
    val timeRangesForToday: List<TimeRangeDto>,
    val timeRangesForTomorrow: List<TimeRangeDto>
)
