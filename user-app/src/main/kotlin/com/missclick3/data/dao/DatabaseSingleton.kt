package com.missclick3.data.dao

import com.missclick3.data.models.Certificates
import com.missclick3.data.models.UserSavedNewsId
import com.missclick3.data.models.Users
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseSingleton {
    fun init() {
        val database =  Database.connect(
            url = System.getenv("DB_URL"),
            driver = System.getenv("DB_DRIVER"),
            user = System.getenv("DB_USER"),
            password = System.getenv("DB_PW")
        )
        transaction (database) {
            SchemaUtils.create(Users)
            SchemaUtils.create(Certificates)
            SchemaUtils.create(UserSavedNewsId)
        }
    }
    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}