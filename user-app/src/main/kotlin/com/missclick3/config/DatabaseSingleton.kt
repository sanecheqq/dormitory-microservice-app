package com.missclick3.config

import com.missclick3.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
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
            SchemaUtils.create(
                UsersTable,
                FluoroCertificatesTable,
                STDsCertificatesTable,
                SavedNewsTable,
                DormitoryAddressesTable
            )
        }
        GlobalScope.launch {
            dbQuery { DormitoryAddressesTable.insert {
                it[address] = "TestStreet123"
            } }
        }
    }
    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}