package ru.missclick3.messages.dtos

import kotlinx.serialization.Serializable

@Serializable
data class TimeRangeDto(
    val id : String? = "",
    val startTime: String,
    val endTime: String,
    val date: String,
    val userId: String? = "",
    val userTg: String? = "",
    val wmNumber: Int,
    val withDrier: Boolean
)
