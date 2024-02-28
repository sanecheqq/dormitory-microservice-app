package com.missclick3.security.token

interface TokenService {
    fun generateToken(
        config: TokenConfig,
        vararg claims: TokenClaim
    ) : String
}