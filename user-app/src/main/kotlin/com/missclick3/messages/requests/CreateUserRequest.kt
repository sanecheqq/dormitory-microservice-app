package com.missclick3.messages.requests

import kotlinx.serialization.Serializable

@Serializable
data class CreateUserRequest(
    val name: String,
    val surname: String,
    val patronymic: String?,
    val address: String,
    val username: String,
    val password: String
)