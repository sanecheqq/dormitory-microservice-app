package com.missclick3.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.missclick3.messages.dtos.UserDTO
import com.missclick3.messages.requests.CreateUserRequest
import com.missclick3.security.hashing.HashingService
import com.missclick3.services.user.UserService

fun Route.adminRoutes(
    hashingService: HashingService,
    userService: UserService
) {
    route("/users/admin") {
        post("/add-user") {
            val request = call.receiveNullable<CreateUserRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, "Некорректный запрос")
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

            val userDTO = UserDTO(
                username = request.username,
                name = request.name,
                surname = request.surname,
                patronymic = request.patronymic,
                address = request.address,
                password = saltedHash.hash,
                salt = saltedHash.salt
            )

            val inserted = userService.createNewUser(userDTO)

            if (!inserted) {
                call.respond(HttpStatusCode.Conflict, "Пользователь ${userDTO.username} не добавлен")
                return@post
            }

            call.respond(HttpStatusCode.OK, userDTO)
        }

        get ("/students") {
            call.respond(HttpStatusCode.OK, message = userService.getUsers())
        }
    }
}