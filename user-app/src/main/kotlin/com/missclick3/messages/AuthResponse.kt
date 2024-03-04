package com.missclick3.messages

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: String
)
