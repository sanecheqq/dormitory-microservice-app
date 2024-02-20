package com.missclick3.data.models

import org.jetbrains.exposed.dao.id.IntIdTable

data class SavedNews(
    val newsId: String
)

object UserSavedNewsId: IntIdTable("user_saved_news_id") {
    val newsId = varchar("newsId", 128)
    val userId = reference("userId", Users)
}
