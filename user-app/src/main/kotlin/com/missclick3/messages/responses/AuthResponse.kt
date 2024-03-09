package com.missclick3.messages.responses

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: String
)