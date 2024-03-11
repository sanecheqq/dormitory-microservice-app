package com.missclick3

import com.missclick3.config.DatabaseSingleton
import com.missclick3.plugins.*
import com.missclick3.repositories.certificates.FluoroCertificateRepositoryImpl
import com.missclick3.repositories.certificates.STDsCertificateRepositoryImpl
import com.missclick3.security.hashing.HashingServiceImpl
import com.missclick3.security.token.TokenConfig
import com.missclick3.security.token.TokenServiceImpl
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.missclick3.repositories.user.UserRepositoryImpl
import com.missclick3.services.certificates.FluoroCertificateServiceImpl
import com.missclick3.services.certificates.STDsCertificateServiceImpl
import com.missclick3.services.user.UserServiceImpl
import com.orbitz.consul.Consul
import com.orbitz.consul.model.agent.ImmutableRegistration


fun main() {
    val server = embeddedServer(Netty, port = 8085, host = "0.0.0.0", module = Application::module)
    val consulClient = Consul.builder().withUrl("http://localhost:8500").build()
    val service = ImmutableRegistration.builder()
        .id("user-${server.environment.connectors[0].port}")
        .name("user-app")
        .address("localhost")
        .port(server.environment.connectors[0].port)
        .build()
    consulClient.agentClient().register(service)
    server.start(wait = true)
}

fun Application.module() {
    val tokenConfig = TokenConfig(
        issuer = System.getenv("JWT_ISSUER"),
        audience = System.getenv("JWT_AUDIENCE"),
        expiresIn = 365L * 1000L * 60L * 60L * 24L,
        secret = System.getenv("JWT_SECRET")
    )
    val userRepository = UserRepositoryImpl()
    val userService = UserServiceImpl(userRepository)

    val fluroRepository = FluoroCertificateRepositoryImpl()
    val fluroService = FluoroCertificateServiceImpl(fluroRepository)

    val stdsRepository = STDsCertificateRepositoryImpl()
    val stdsService = STDsCertificateServiceImpl(stdsRepository)

    val hashingService = HashingServiceImpl()
    val tokenService = TokenServiceImpl()
    DatabaseSingleton.init()
    configureSecurity(tokenConfig)
    configureSerialization()
    configureRouting(userService, hashingService, fluroService, stdsService)
}
