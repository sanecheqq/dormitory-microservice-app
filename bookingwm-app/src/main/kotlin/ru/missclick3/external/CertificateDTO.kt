package ru.missclick3.external

import kotlinx.serialization.Serializable

@Serializable
data class CertificateDTO(
    val id: String? = "",
    val startDate: String,
    val expireDate: String
)