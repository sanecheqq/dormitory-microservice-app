package com.missclick3.services.saved_news

import com.missclick3.model.SavedNews
import java.util.*

interface SavedNewsService {
    suspend fun getSavedNews(userId: UUID): List<SavedNews>

    suspend fun addToSavedNews(newsId: UUID, userId: UUID): Boolean

    suspend fun deleteFromSavedNews(id: UUID): Boolean
}