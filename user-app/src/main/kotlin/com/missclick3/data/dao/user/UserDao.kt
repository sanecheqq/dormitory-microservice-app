package com.missclick3.data.dao.user

import com.missclick3.data.models.User
import java.util.UUID

interface UserDao {
    suspend fun insertNewUser(user: User): Boolean
    suspend fun getUserByUsername(username: String): User?

    suspend fun updateUser(user: User) : Boolean

    suspend fun getUserById(id: UUID) : User?

    suspend fun deleteUser(username: String)
}