package com.missclick3.repositories.saved_news

import com.missclick3.config.DatabaseSingleton.dbQuery
import com.missclick3.model.SavedNews
import com.missclick3.model.SavedNewsTable
import com.missclick3.model.User
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import java.util.*

class SavedNewsRepositoryImpl : SavedNewsRepository {
    override suspend fun getSavedNews(userId: UUID): List<String> {
        return dbQuery {
            SavedNews.find { SavedNewsTable.userId eq userId }
                .map { it.newsId }
                .toList()
        }
    }

    override suspend fun addToSavedNews(newsId: UUID, userId: UUID): Boolean {
        return try {
            dbQuery {
                val savedNews = SavedNews.find {SavedNewsTable.newsId eq newsId.toString() and (SavedNewsTable.userId eq userId)}.singleOrNull()
                if (savedNews == null) {
                    SavedNews.new {
                        this.newsId = newsId.toString()
                        user = User.findById(userId)!!
                    }
                }
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun deleteNewsReferenceForAllUsers(newsId: String): Boolean {
        return try {
            dbQuery {
                SavedNews.find { SavedNewsTable.newsId eq newsId }
                    .toList()
                    .forEach(SavedNews::delete)
                true
            }
        } catch (e: Exception) {
            false
        }
    }



    override suspend fun deleteFromSavedNews(newsId: String, userId: UUID): Boolean {
        return try {
            dbQuery {
                SavedNews.find {SavedNewsTable.newsId eq newsId and (SavedNewsTable.userId eq userId)}.singleOrNull()?.delete()
            }
            true
        } catch (e: Exception) {
            false
        }
    }
}