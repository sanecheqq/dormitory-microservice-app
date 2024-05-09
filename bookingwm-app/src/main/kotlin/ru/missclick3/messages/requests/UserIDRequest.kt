package ru.missclick3.messages.requests

import kotlinx.serialization.Serializable

@Serializable
data class UserIDRequest(
    val userId: String
)
