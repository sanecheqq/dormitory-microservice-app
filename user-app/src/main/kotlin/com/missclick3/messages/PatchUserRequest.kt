package com.missclick3.messages

import kotlinx.serialization.Serializable

@Serializable
data class PatchUserRequest(
    val email: String?,
    val phoneNumber: String?,
    val tgUsername: String?
)
