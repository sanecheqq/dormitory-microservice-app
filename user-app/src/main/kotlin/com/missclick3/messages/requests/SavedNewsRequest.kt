package com.missclick3.messages.requests

import kotlinx.serialization.Serializable

@Serializable
data class SavedNewsRequest(
    val newsId: String
)