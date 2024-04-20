package com.missclick3.services.saved_product

import com.missclick3.repositories.saved_products.SavedProductRepository
import java.util.*

class SavedProductServiceImpl(
    private val repository: SavedProductRepository
) : SavedProductService {
    override suspend fun getSavedProducts(userId: UUID): List<String> {
        return repository.getSavedProducts(userId)
    }

    override suspend fun addToSavedProducts(productId: UUID, userId: UUID): Boolean {
        return repository.addToSavedProducts(productId, userId)
    }

    override suspend fun deleteFromSavedProducts(productId: String, userId: UUID): Boolean {
        return repository.deleteFromSavedProducts(productId, userId)
    }

    override suspend fun deleteProductReferenceForAllUsers(productId: String): Boolean {
        return repository.deleteProductsReferenceForAllUsers(productId)
    }

}