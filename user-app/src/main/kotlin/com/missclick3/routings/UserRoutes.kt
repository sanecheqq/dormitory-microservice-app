package com.missclick3.routings

import com.missclick3.data.dao.user.UserDao
import com.missclick3.data.models.User
import com.missclick3.messages.*
import com.missclick3.security.hashing.HashingService
import com.missclick3.security.hashing.SaltedHash
import com.missclick3.security.token.TokenClaim
import com.missclick3.security.token.TokenConfig
import com.missclick3.security.token.TokenService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.apache.commons.validator.routines.EmailValidator
import java.util.*

fun Route.signIn(
    dao: UserDao,
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {
    post("/signin") {
        val request = call.receiveNullable<AuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val user = dao.getUserByUsername(request.username)
        if (user == null) {
            call.respond(HttpStatusCode.Conflict)
            return@post
        }

        val isPasswordValid = hashingService.verifyHash(
            value = request.password,
            saltedHash = SaltedHash(
                hash = user.password,
                salt = user.salt
            )
        )

        if (!isPasswordValid) {
            call.respond(HttpStatusCode.Conflict, "Password is invalid")
            return@post
        }

        var token = tokenService.generateToken(
            config = tokenConfig,
            TokenClaim(
                name = "userId",
                value = user.id!!
            )
        )

        call.respond(HttpStatusCode.OK, message = AuthResponse(token = token))
    }
}

fun Route.authenticate() {
    authenticate("myAuth") {
        get("/authenticate") {
            call.respond(HttpStatusCode.OK)
        }
    }
}

fun Route.secretRoutes(dao: UserDao) {
    authenticate("myAuth") {
        get("/user") {
            val principal = call.principal<JWTPrincipal>()
            val userId = try {
                UUID.fromString(principal?.getClaim("userId", String::class).toString())
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict)
                return@get
            }

            val user = dao.getUserById(userId)
            if (user == null) {
                call.respond(HttpStatusCode.Conflict, "No such user")
                return@get
            }

            call.respond(
                HttpStatusCode.OK,
                message = GetUserResponse(
                    name = user.name,
                    surname = user.surname,
                    patronymic = user.patronymic,
                    email = user.email,
                    phoneNumber = user.phoneNumber,
                    tgUsername = user.tgUsername,
                    address = user.address
                )
            )
        }
        patch("/user") {
            val request = call.receiveNullable<PatchUserRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@patch
            }

            val isEmailValid = EmailValidator.getInstance().isValid(request.email!!)
                    || request.email.isBlank()
            val isPhoneNumberValid = Regex("^\\+7\\d{10}$").matches(request.phoneNumber!!)
                    || request.phoneNumber.isBlank()
            val isTgUsernameValid = Regex("^@[a-zA-Z0-9_]{5,32}$").matches(request.tgUsername!!)
                    || request.tgUsername.isBlank()

            if (!isEmailValid || !isTgUsernameValid || !isPhoneNumberValid) {
                call.respond(HttpStatusCode.Conflict, "Incorrect options")
            }

            val principal = call.principal<JWTPrincipal>()
            val userId = try {
                UUID.fromString(principal?.getClaim("userId", String::class).toString())
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict)
                return@patch
            }

            val user = dao.getUserById(userId)

            if (user == null) {
                call.respond(HttpStatusCode.Conflict, "No user with id")
            }

            val updated = dao.updateUser(
                userId,
                request.email,
                request.phoneNumber,
                request.tgUsername
            )

            if (!updated) {
                call.respond(HttpStatusCode.Conflict, "User was not updated")
                return@patch
            }

            call.respond(HttpStatusCode.OK, "User with id $userId was updated")
        }
    }
}