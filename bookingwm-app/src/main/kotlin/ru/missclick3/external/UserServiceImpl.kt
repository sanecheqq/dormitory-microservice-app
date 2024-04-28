package ru.missclick3.external

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.apache.*
import io.ktor.client.request.*
import ru.missclick3.util.ConsulFeature

class UserServiceImpl : UserService {
    override suspend fun getUserDto(authHeader: String): UserDTO? {
        val client = HttpClient(Apache) {
            install(ConsulFeature) {
                this.consulUrl = "http://localhost:8500"
            }
        }
        try {
            val response = client.get("consul://user-app/user") {
                header("Authorization", authHeader)
            }.body<UserInfoResponse>().userDTO
            return response
        } catch (e: Exception) {
            return null
        }
    }

    override suspend fun getUserAddress(authHeader: String): String? {
        return getUserDto(authHeader)?.address
    }

    override suspend fun getUserRole(authHeader: String): String? {
        return getUserDto(authHeader)?.role
    }
}