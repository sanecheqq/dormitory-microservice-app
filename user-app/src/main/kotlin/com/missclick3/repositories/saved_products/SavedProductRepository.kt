package com.missclick3.repositories.saved_products

import java.util.*

interface SavedProductRepository {
    suspend fun getSavedProducts(userId: UUID) : List<String>

    suspend fun addToSavedProducts(productId: UUID, userId: UUID) : Boolean

    suspend fun deleteProductsReferenceForAllUsers(productId: String) : Boolean

    suspend fun deleteFromSavedProducts(productId: String, userId: UUID) : Boolean
}