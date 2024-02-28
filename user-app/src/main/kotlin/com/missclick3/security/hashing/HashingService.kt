package com.missclick3.security.hashing

interface HashingService {
    fun generateSaltedHash(
        value: String,
        saltedLength: Int = 32
    ) : SaltedHash
    fun verifyHash(
        value: String,
        saltedHash: SaltedHash
    ) : Boolean
}