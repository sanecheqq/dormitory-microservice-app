package com.missclick3.plugins

import com.missclick3.model.FluoroCertificate
import com.missclick3.model.STDsCertificate
import com.missclick3.routes.*
import com.missclick3.security.hashing.HashingService
import com.missclick3.security.token.TokenConfig
import com.missclick3.security.token.TokenService
import io.ktor.server.application.*
import io.ktor.server.routing.*
import com.missclick3.services.certificates.CertificateService
import com.missclick3.services.saved_news.SavedNewsService
import com.missclick3.services.user.UserService

fun Application.configureRouting(
    userService: UserService,
    hashingService: HashingService,
    fluoroService: CertificateService<FluoroCertificate>,
    stdsService: CertificateService<STDsCertificate>,
    tokenService: TokenService,
    tokenConfig: TokenConfig,
    savedNewsService: SavedNewsService
) {
    routing {
        adminRoutes(hashingService, userService, fluoroService, stdsService)
        authRoutes(tokenConfig, hashingService, tokenService, userService)
        savedNewsRoutes(savedNewsService)
        authenticate()
        userRoutes(userService, fluoroService, stdsService, savedNewsService)
    }
}
