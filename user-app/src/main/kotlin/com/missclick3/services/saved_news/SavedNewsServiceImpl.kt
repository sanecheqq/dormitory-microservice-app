package com.missclick3.services.saved_news

import com.missclick3.model.SavedNews
import com.missclick3.repositories.saved_news.SavedNewsRepository
import com.missclick3.services.saved_news.SavedNewsService
import java.util.*

class SavedNewsServiceImpl(savedNewsRepository: SavedNewsRepository) : SavedNewsService {
    private val repository = savedNewsRepository

    override suspend fun getSavedNews(userId: UUID): List<String> {
        return repository.getSavedNews(userId)
    }

    override suspend fun addToSavedNews(newsId: UUID, userId: UUID): Boolean {
        return repository.addToSavedNews(newsId, userId)
    }

    override suspend fun deleteNewsReferenceForAllUsers(newsId: String): Boolean {
        return repository.deleteNewsReferenceForAllUsers(newsId)
    }

    override suspend fun deleteFromSavedNews(newsId: String, userId: UUID): Boolean {
        return repository.deleteFromSavedNews(newsId, userId)
    }
}