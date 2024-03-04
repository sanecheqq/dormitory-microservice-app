package com.missclick3.messages

import kotlinx.serialization.Serializable

@Serializable
data class AddUserRequest(
    val name: String,
    val surname: String,
    val patronymic: String?,
    val address: String,
    val username: String,
    val password: String
)
