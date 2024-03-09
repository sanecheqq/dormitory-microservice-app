package com.missclick3.model

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.*

object UsersTable: UUIDTable("users") {
    val username = varchar("username", 128).uniqueIndex()
    val name = varchar("name", 128)
    val surname = varchar("surname", 128)
    val patronymic = varchar("patronymic", 128).nullable()
    val email = varchar("email", 256).nullable()//.check { it like "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$" }
    val phoneNumber = varchar("phoneNumber", 12).nullable()//.check { it like "\"^\\\\+7\\\\d{10}\$\""}
    val tgUsername = varchar("tgUsername", 128).nullable()
    val password = varchar("password", 128)
    val salt = varchar("salt", 128)

    val address = reference("address", DormitoryAddressesTable)
}

class User(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object: UUIDEntityClass<User>(UsersTable)

    var username by UsersTable.username
    var name by UsersTable.name
    var surname by UsersTable.surname
    var patronymic by UsersTable.patronymic
    var email by UsersTable.email
    var phoneNumber by UsersTable.phoneNumber
    var tgUsername by UsersTable.tgUsername
    var password by UsersTable.password
    var salt by UsersTable.salt

    var address by DormitoryAddress referencedOn UsersTable.address
}