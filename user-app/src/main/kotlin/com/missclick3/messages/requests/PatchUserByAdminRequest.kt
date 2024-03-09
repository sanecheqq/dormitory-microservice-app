package com.missclick3.messages.requests

import kotlinx.serialization.Serializable

@Serializable
data class PatchUserByAdminRequest(
    val username: String?,
    val name: String?,
    val surname: String?,
    val patronymic: String?,
    val address: String?
)