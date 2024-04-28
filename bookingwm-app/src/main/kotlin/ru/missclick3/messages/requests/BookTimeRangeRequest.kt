package ru.missclick3.messages.requests

import kotlinx.serialization.Serializable

@Serializable
data class BookTimeRangeRequest(
    val address: String,
    val wmNumber: Int,
    val userId: String,
    val userTg: String,
    val startTime: String,
    val endTime: String,
    val date: String,
    val withDrier: Boolean
)
