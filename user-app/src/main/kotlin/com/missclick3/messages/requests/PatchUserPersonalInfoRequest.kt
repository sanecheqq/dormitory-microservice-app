package com.missclick3.messages.requests

import kotlinx.serialization.Serializable

@Serializable
data class PatchUserPersonalInfoRequest(
    val email: String?,
    val phoneNumber: String?,
    val tgUsername: String?
)
