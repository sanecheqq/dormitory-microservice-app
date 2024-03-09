package com.missclick3.messages.requests

import kotlinx.serialization.Serializable

@Serializable
data class AddSavedNewsRequest(
    val newsId: String
)