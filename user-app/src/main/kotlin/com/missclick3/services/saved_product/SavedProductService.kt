package com.missclick3.services.saved_product

import java.util.*

interface SavedProductService {
    suspend fun getSavedProducts(userId: UUID) : List<String>
    suspend fun addToSavedProducts(productId: UUID, userId: UUID) : Boolean
    suspend fun deleteFromSavedProducts(productId: String, userId: UUID) : Boolean
    suspend fun deleteProductReferenceForAllUsers(productId: String) : Boolean
}