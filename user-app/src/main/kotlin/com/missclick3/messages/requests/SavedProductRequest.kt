package com.missclick3.messages.requests

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class SavedProductRequest(
    val productId: String
)
