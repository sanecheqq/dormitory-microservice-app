package com.missclick3.data.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import java.util.UUID

@Serializable
data class User(
    val id: String? = "",
    val username: String,
    val name: String,
    val surname: String,
    val patronymic: String? = "",
    val email: String? = "",
    val phoneNumber: String? = "",
    val tgUsername: String? = "",
    val address: String,
    val password: String,
    val salt: String
)

object Users: UUIDTable("users") {
    val username = varchar("username", 128).uniqueIndex()
    val name = varchar("name", 128)
    val surname = varchar("surname", 128)
    val patronymic = varchar("patronymic", 128).nullable()
    val email = varchar("email", 256).nullable()//.check { it like "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$" }
    val phoneNumber = varchar("phoneNumber", 12).nullable()//.check { it like "\"^\\\\+7\\\\d{10}\$\""}
    val tgUsername = varchar("tgUsername", 128).nullable()
    val address = varchar("address", 256)
    val password = varchar("password", 128)
    val salt = varchar("salt", 128)
}
