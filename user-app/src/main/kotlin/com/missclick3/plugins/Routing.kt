package com.missclick3.plugins

import com.missclick3.data.dao.user.UserDao
import com.missclick3.routings.addUser
import com.missclick3.routings.authenticate
import com.missclick3.routings.secretRoutes
import com.missclick3.routings.signIn
import com.missclick3.security.hashing.HashingService
import com.missclick3.security.token.TokenConfig
import com.missclick3.security.token.TokenService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    hashingService: HashingService,
    dao: UserDao,
    tokenConfig: TokenConfig,
    tokenService: TokenService
) {
    routing {
        addUser(hashingService, dao)
        signIn(dao, hashingService, tokenService, tokenConfig)
        authenticate()
        secretRoutes(dao)
    }
}
