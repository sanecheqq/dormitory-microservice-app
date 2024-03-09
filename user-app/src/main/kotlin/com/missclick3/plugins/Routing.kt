package com.missclick3.plugins

import com.missclick3.security.hashing.HashingService
import io.ktor.server.application.*
import io.ktor.server.routing.*
import com.missclick3.routes.adminRoutes
import com.missclick3.services.user.UserService

fun Application.configureRouting(
    userService: UserService,
    hashingService: HashingService
) {
    routing {
        adminRoutes(hashingService, userService)
    }
}
