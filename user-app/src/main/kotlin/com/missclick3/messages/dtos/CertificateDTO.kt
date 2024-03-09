package com.missclick3.messages.dtos

import kotlinx.serialization.Serializable

@Serializable
data class CertificateDTO(
    val id: String? = "",
    val startDate: String,
    val expireDate: String
)