package ru.missclick3.external

import kotlinx.serialization.Serializable

@Serializable
data class UserInfoResponse(
    val userDTO: UserDTO,
    val fluoroCertificateDTO: CertificateDTO?,
    val stdsCertificateDTO: CertificateDTO?
)
