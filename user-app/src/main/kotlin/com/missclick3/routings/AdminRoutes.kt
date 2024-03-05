package com.missclick3.routings

import com.missclick3.data.dao.user.UserDao
import com.missclick3.data.models.User
import com.missclick3.messages.AddUserRequest
import com.missclick3.security.hashing.HashingService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.addUser(
    hashingService: HashingService,
    dao: UserDao
) {
    post("/add-user") {
        val request = call.receiveNullable<AddUserRequest>() ?:kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val areFieldsBlank = request.name.isBlank() || request.surname.isBlank() || request.address.isBlank()

        if (areFieldsBlank) {
            call.respond(HttpStatusCode.Conflict, "Поля пустые")
            return@post
        }

        val isUsernameBlank = request.username.isBlank()
        val isPasswordValid = request.password.length >= 8
                || request.password.any { it.isUpperCase() }
                || request.password.any { it.isDigit() }
                || request.password.any { !it.isLetterOrDigit() }

        if (isUsernameBlank || !isPasswordValid) {
            call.respond(HttpStatusCode.Conflict, "Некорректный логин или пароль")
            return@post
        }

        val saltedHash = hashingService.generateSaltedHash(request.password)
        val user = User(
            name = request.name,
            surname = request.surname,
            patronymic = request.patronymic,
            username = request.username,
            address = request.address,
            password = saltedHash.hash,
            salt = saltedHash.salt
        )

        val inserted = dao.insertNewUser(user)
        if (!inserted) {
            call.respond(HttpStatusCode.Conflict, "Пользователь ${user.username} не добавлен")
            return@post
        }

        call.respond(HttpStatusCode.OK)
    }
}