package com.missclick3.messages.requests

import com.missclick3.messages.dtos.CertificateDTO
import kotlinx.serialization.Serializable

@Serializable
data class PatchUserByAdminRequest(
    val username: String,
    val name: String,
    val role: String,
    val surname: String,
    val patronymic: String?,
    val address: String,
    val fluroCert: CertificateDTO?,
    val stdsCert: CertificateDTO?
)