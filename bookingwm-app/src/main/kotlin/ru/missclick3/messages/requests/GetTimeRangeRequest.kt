package ru.missclick3.messages.requests

import kotlinx.serialization.Serializable

@Serializable
data class GetTimeRangeRequest(
    val address: String,
    val wmNumber: Int,
    val startTime: String,
    val endTime: String,
    val date: String
)
