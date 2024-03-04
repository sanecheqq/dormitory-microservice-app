package com.missclick3.routings

import com.missclick3.data.dao.user.UserDao
import com.missclick3.messages.AddUserRequest
import com.missclick3.messages.AuthRequest
import com.missclick3.security.hashing.HashingService
import com.missclick3.security.hashing.SaltedHash
import com.missclick3.security.token.TokenClaim
import com.missclick3.security.token.TokenConfig
import com.missclick3.security.token.TokenService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.singIn(
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
                name = "UserId",
                value = user.id!!
            )
        )
    }
}