package ru.missclick3.external

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.apache.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import ru.missclick3.util.ConsulFeature

class UserServiceImpl : UserService {
    override suspend fun getUserDto(authHeader: String): UserDTO? {
        val client = HttpClient(Apache) {
            install(ConsulFeature) {
                this.consulUrl = "http://localhost:8500"
            }
            install(ContentNegotiation) {
                json()
            }
        }
        try {
            val response = client.get("http://user-app/user") {
                header("Authorization", authHeader)
            }.body<UserInfoResponse>().userDTO
            return response
        } catch (e: Exception) {
            println("EXCEPTION!!! ${e.message}")
            return null
        }
        finally {
            client.close()
        }
    }

    override suspend fun getUserAddress(authHeader: String): String? {
        return getUserDto(authHeader)?.address
    }

    override suspend fun getUserRole(authHeader: String): String? {
        return getUserDto(authHeader)?.role
    }
}