package com.missclick3.messages

import kotlinx.serialization.Serializable

@Serializable
data class GetUserResponse(
    val name: String,
    val surname: String,
    val patronymic: String? = "",
    val email: String? = "",
    val phoneNumber: String? = "",
    val tgUsername: String? = "",
    val address: String
)
