package com.missclick3.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like

@Serializable
data class User(
    val id: String? = "",
    val name: String,
    val surname: String,
    val patronymic: String,
    val email: String,
    val phoneNumber: String,
    val tgUsername: String,
    val address: String
)

object Users: UUIDTable("users") {
    val name = varchar("name", 128)
    val surname = varchar("surname", 128)
    val patronymic = varchar("patronymic", 128)
    val email = varchar("email", 256).like("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$")
    val phoneNumber = varchar("phoneNumber", 12).like("\"^\\\\+7\\\\d{10}\$\"")
    val tgUsername = varchar("tgUsername", 128)
}
