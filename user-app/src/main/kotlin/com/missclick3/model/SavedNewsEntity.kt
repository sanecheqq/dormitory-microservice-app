package com.missclick3.model

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.*

object SavedNewsTable: UUIDTable("user_saved_news_id") {
    val newsId = varchar("newsId", 128)
    val userId = reference("userId", UsersTable)
}

class SavedNews(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<SavedNews>(SavedNewsTable)

    var newsId by SavedNewsTable.newsId
    var user by User referencedOn SavedNewsTable.userId
}