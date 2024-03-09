package com.missclick3.messages.responses

import com.missclick3.messages.dtos.CertificateDTO
import com.missclick3.messages.dtos.UserDTO
import kotlinx.serialization.Serializable

@Serializable
data class UserResponseFromAdmin(
    val userDTO: UserDTO,
    val fluoroCertificateDTO: CertificateDTO?,
    val stdsCertificateDTO: CertificateDTO?
)