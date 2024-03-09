package com.missclick3.repositories.saved_news

import com.missclick3.config.DatabaseSingleton.dbQuery
import com.missclick3.model.SavedNews
import com.missclick3.model.SavedNewsTable
import com.missclick3.model.User
import java.util.*

class SavedNewsRepositoryImpl : SavedNewsRepository {
    override suspend fun getSavedNews(userId: UUID): List<SavedNews> {
        return dbQuery {
            SavedNews.find { SavedNewsTable.userId eq userId }.toList()
        }
    }

    override suspend fun addToSavedNews(newsId: UUID, userId: UUID): Boolean {
        return try {
            dbQuery {
                SavedNews.new {
                    this.newsId = newsId.toString()
                    user = User.findById(userId)!!
                }
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun deleteFromSavedNews(id: UUID): Boolean {
        return try {
            dbQuery {
                SavedNews.findById(id)?.delete()
            }
            true
        } catch (e: Exception) {
            false
        }
    }
}