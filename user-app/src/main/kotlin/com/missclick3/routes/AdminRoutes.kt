package com.missclick3.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.missclick3.messages.dtos.UserDTO
import com.missclick3.messages.requests.CreateUserRequest
import com.missclick3.messages.requests.PatchUserByAdminRequest
import com.missclick3.messages.responses.UserInfoResponse
import com.missclick3.model.FluoroCertificate
import com.missclick3.model.STDsCertificate
import com.missclick3.routes.authenticate
import com.missclick3.security.hashing.HashingService
import com.missclick3.services.certificates.CertificateService
import com.missclick3.services.user.UserService
import com.missclick3.util.UserRole
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import java.util.*

fun Route.adminRoutes(
    hashingService: HashingService,
    userService: UserService,
    fluoroService: CertificateService<FluoroCertificate>,
    stdsService: CertificateService<STDsCertificate>
) {

    authenticate("myAuth") {
        route("/admin") {
            intercept(ApplicationCallPipeline.Call) {
                val principal = call.principal<JWTPrincipal>()
                val userId = try {
                    UUID.fromString(principal?.getClaim("userId", String::class).toString())
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.Conflict)
                    return@intercept finish()
                }
                // Добавьте вашу дополнительную проверку здесь, например, проверку роли пользователя
                val user = userService.getUserById(userId)
                if (user == null || user.role != "ADMIN") {
                    call.respond(HttpStatusCode.Conflict, "You are not an admin")
                    return@intercept finish()
                }
            }
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
                    salt = saltedHash.salt,
                    role = request.role
                )

                val inserted = userService.createNewUser(userDTO)
                if (!inserted) {
                    call.respond(HttpStatusCode.Conflict, "Пользователь ${userDTO.username} не добавлен")
                    return@post
                }

                call.respond(
                    HttpStatusCode.OK,
                    UserDTO(
                        username = request.username,
                        role = request.role,
                        name = request.name,
                        surname = request.surname,
                        patronymic = request.patronymic,
                        address = request.address,
                        password = "",
                        salt = ""
                    )
                )

            }
            route("/students"){
                get () {
                    call.respond(HttpStatusCode.OK, message = userService.getUsers())
                }

                get("/{id}") {
                    val id = call.parameters["id"] ?: return@get call.respond(
                        HttpStatusCode.BadRequest,
                        "Id is missing"
                    )
                    val userId = UUID.fromString(id)
                    val userDTO = userService.getUserById(userId)
                    val fluoroDTO = fluoroService.getCertificateByUserId(userId)
                    val stdsDTO = stdsService.getCertificateByUserId(userId)
                    if (userDTO == null) {
                        call.respond(HttpStatusCode.Conflict, "No user with id: $id")
                        return@get
                    }

                    call.respond(
                        HttpStatusCode.OK,
                        UserInfoResponse(
                            userDTO,
                            fluoroDTO,
                            stdsDTO
                        )
                    )
                }

                patch("/{id}") {
                    val id = call.parameters["id"] ?: return@patch call.respond(
                        HttpStatusCode.BadRequest,
                        "Id is missing"
                    )

                    val request = call.receiveNullable<PatchUserByAdminRequest>() ?: kotlin.run {
                        call.respond(HttpStatusCode.BadRequest, "BAD REQUEST")
                        return@patch
                    }

                    val userId = UUID.fromString(id)

                    val updated = userService.updateUser(
                        userId,
                        request
                    )

                    if (!updated) {
                        call.respond(HttpStatusCode.Conflict, "not able to update user")
                        return@patch
                    }

                    call.respond(HttpStatusCode.OK, "User have been changed")
                }

                delete("/{id}") {
                    val id = call.parameters["id"] ?: return@delete call.respond(
                        HttpStatusCode.BadRequest,
                        "Id is missing"
                    )
                    val userId = UUID.fromString(id)

                    val deleted = userService.deleteUser(userId)

                    if (!deleted) {
                        call.respond(HttpStatusCode.Conflict, "user was not deleted")
                        return@delete
                    }

                    call.respond(HttpStatusCode.OK, "user was deleted")
                }
            }
        }
    }
}