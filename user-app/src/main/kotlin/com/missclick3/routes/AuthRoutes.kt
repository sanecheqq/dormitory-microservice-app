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
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authRoutes(
    tokenConfig: TokenConfig,
    hashingService: HashingService,
    tokenService: TokenService,
    userService: UserService
) {
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
                )
            )

            call.respond(HttpStatusCode.OK, AuthResponse(token = token))
        }
    }
}