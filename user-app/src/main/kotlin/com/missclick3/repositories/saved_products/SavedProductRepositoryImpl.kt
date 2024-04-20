package com.missclick3.repositories.saved_products

import com.missclick3.config.DatabaseSingleton.dbQuery
import com.missclick3.model.SavedProduct
import com.missclick3.model.SavedProductsTable
import com.missclick3.model.User
import org.jetbrains.exposed.sql.and
import java.util.*

class SavedProductRepositoryImpl : SavedProductRepository {
    override suspend fun getSavedProducts(userId: UUID): List<String> {
        return dbQuery {
            SavedProduct.find { SavedProductsTable.userId eq userId }
                .map { it.productId }
                .toList()
        }
    }

    override suspend fun addToSavedProducts(productId: UUID, userId: UUID): Boolean {
        return try {
            dbQuery {
                val savedProducts = SavedProduct.find {
                    SavedProductsTable.productId eq productId.toString() and (SavedProductsTable.userId eq userId)
                }.singleOrNull()

                if (savedProducts == null) {
                    SavedProduct.new {
                        this.productId = productId.toString()
                        user = User.findById(userId)!!
                    }
                }
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun deleteProductsReferenceForAllUsers(productId: String): Boolean {
        return try {
            dbQuery {
                SavedProduct.find { SavedProductsTable.productId eq productId }
                    .forEach(SavedProduct::delete)
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun deleteFromSavedProducts(productId: String, userId: UUID): Boolean {
        return try {
            dbQuery {
                SavedProduct.find {
                    SavedProductsTable.productId eq productId and (SavedProductsTable.userId eq userId)
                }.singleOrNull()?.delete()
            }
            true
        } catch (e: Exception) {
            false
        }
    }
}