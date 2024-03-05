package com.missclick3

import com.missclick3.data.dao.DatabaseSingleton
import com.missclick3.data.dao.user.UserDaoImpl
import com.missclick3.data.models.User
import com.missclick3.plugins.*
import com.missclick3.security.hashing.HashingServiceImpl
import com.missclick3.security.token.TokenConfig
import com.missclick3.security.token.TokenServiceImpl
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val tokenConfig = TokenConfig(
        issuer = System.getenv("JWT_ISSUER"),
        audience = System.getenv("JWT_AUDIENCE"),
        expiresIn = 365L * 1000L * 60L * 60L * 24L,
        secret = System.getenv("JWT_SECRET")
    )
    val dao = UserDaoImpl()
    val hashingService = HashingServiceImpl()
    val tokenService = TokenServiceImpl()
    DatabaseSingleton.init()
    configureSecurity(tokenConfig)
    configureMonitoring()
    configureSerialization()
    configureRouting(hashingService, dao, tokenConfig, tokenService)
}
