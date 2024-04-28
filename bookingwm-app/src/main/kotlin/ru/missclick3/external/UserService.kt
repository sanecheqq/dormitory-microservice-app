package ru.missclick3.external


interface UserService {
    suspend fun getUserDto(authHeader: String) : UserDTO?

    suspend fun getUserRole(authHeader: String) : String?

    suspend fun getUserAddress(authHeader: String) : String?
}