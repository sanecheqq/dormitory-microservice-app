package ru.missclick3.messages.requests

import kotlinx.serialization.Serializable

@Serializable
data class GetTimeRangesForUserRequest(
    val userId: String
)
