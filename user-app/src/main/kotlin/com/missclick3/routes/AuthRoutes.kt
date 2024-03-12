package com.missclick3.routes

import com.missclick3.messages.requests.LoginRequest
import com.missclick3.messages.responses.AuthResponse
import com.missclick3.model.FluoroCertificate
import com.missclick3.model.STDsCertificate
import com.missclick3.security.hashing.HashingService
import com.missclick3.security.hashing.SaltedHash
import com.missclick3.security.token.TokenClaim
import com.missclick3.security.token.TokenConfig
import com.missclick3.security.token.TokenService
import com.missclick3.services.certificates.CertificateService
import com.missclick3.services.user.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Route.authRoutes(
    tokenConfig: TokenConfig,
    hashingService: HashingService,
    tokenService: TokenService,
    userService: UserService
) {
    authenticate("myAuth") {
        get("/authenticate") {
            val principal = call.principal<JWTPrincipal>()
            val userRole = try {
                principal?.getClaim("userRole", String::class)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict)
                return@get
            }
            if (userRole == null) {
                call.respond(HttpStatusCode.Conflict, "Ты никто")
                return@get
            }
            call.respond(HttpStatusCode.OK, userRole)
        }
    }

    route("/auth") {
        post("/login") {
            val request = call.receiveNullable<LoginRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, "invalid request")
                return@post
            }

            val userDTO = userService.getUserByUsername(request.username)

            if (userDTO == null) {
                call.respond(HttpStatusCode.Conflict, "no user found")
                return@post
            }

            val isPasswordValid = hashingService.verifyHash(
                value = request.password,
                saltedHash = SaltedHash(
                    hash = userDTO.password!!,
                    salt = userDTO.salt!!
                )
            )

            if (!isPasswordValid) {
                call.respond(HttpStatusCode.Conflict, "invalid password")
                return@post
            }

            val token = tokenService.generateToken(
                config = tokenConfig,
                TokenClaim(
                    name = "userId",
                    value = userDTO.id!!
                ),
                TokenClaim(
                    name = "userRole",
                    value = userDTO.role
                )
            )

            call.respond(HttpStatusCode.OK, AuthResponse(token = token))
        }
    }
}