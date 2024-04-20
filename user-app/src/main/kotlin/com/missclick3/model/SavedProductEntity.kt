package com.missclick3.model

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.*

object SavedProductsTable : UUIDTable("saved_products") {
    val productId = varchar("productId", 128)
    val userId = reference("userId", UsersTable)
}

class SavedProduct(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<SavedProduct>(SavedProductsTable)

    var productId by SavedProductsTable.productId
    var user by User referencedOn SavedProductsTable.userId
}