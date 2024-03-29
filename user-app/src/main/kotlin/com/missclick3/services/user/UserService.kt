package com.missclick3.services.user

import com.missclick3.messages.dtos.UserDTO
import com.missclick3.messages.requests.PatchUserByAdminRequest
import com.missclick3.messages.requests.PatchUserPersonalInfoRequest
import com.missclick3.model.User
import java.util.UUID

interface UserService {
    suspend fun createNewUser(userDTO: UserDTO): Boolean

    suspend fun getUserById(id: UUID): UserDTO?

    suspend fun getUserByUsername(username: String): UserDTO?

    suspend fun updateUserPersonalInfo(id: UUID, request: PatchUserPersonalInfoRequest): Boolean

    suspend fun updateUser(id: UUID, request: PatchUserByAdminRequest): Boolean

    suspend fun deleteUser(id: UUID): Boolean

    suspend fun getUsers(): List<UserDTO>
}