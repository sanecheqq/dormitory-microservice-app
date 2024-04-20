package com.missclick3.messages.requests

import kotlinx.serialization.Serializable

@Serializable
data class DeleteReferencesForSavedProduct(
    val deletedProductUserId: String,
    val productId: String
)