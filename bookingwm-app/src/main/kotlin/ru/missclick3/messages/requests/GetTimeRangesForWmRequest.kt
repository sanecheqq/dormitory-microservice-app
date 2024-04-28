package ru.missclick3.messages.requests

import kotlinx.serialization.Serializable

@Serializable
data class GetTimeRangesForWmRequest(
    val address: String,
    val wmNumber: Int,
    val date: String
)