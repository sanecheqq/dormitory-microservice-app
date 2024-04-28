package ru.missclick3.messages.requests

import kotlinx.serialization.Serializable

@Serializable
data class WashingMachineRequest(
    val address: String,
    val wmNumber: Int
)
