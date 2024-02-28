package com.missclick3.security.hashing

data class SaltedHash(
    val hash: String,
    val salt: String
)
