package ru.missclick3.messages.dtos

import kotlinx.serialization.Serializable

@Serializable
data class WashingMachineDto(
    val id: String? = "",
    val address: String,
    val wmNumber: Int,
    val enabled: Boolean
)
