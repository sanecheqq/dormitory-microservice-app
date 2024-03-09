package com.missclick3.services.user

import com.missclick3.config.DatabaseSingleton.dbQuery
import com.missclick3.messages.dtos.UserDTO
import com.missclick3.messages.requests.PatchUserByAdminRequest
import com.missclick3.messages.requests.PatchUserPersonalInfoRequest
import com.missclick3.model.User
import com.missclick3.repositories.user.UserRepository
import com.missclick3.services.user.UserService
import java.util.*

class UserServiceImpl(userRepository: UserRepository) : UserService {
    private val repository = userRepository

    private suspend fun userToUserDTO(user: User) = UserDTO(
        username = user.username,
        name = user.name,
        surname = user.surname,
        patronymic = user.patronymic,
        id = user.id.toString(),
        email = user.email,
        phoneNumber = user.phoneNumber,
        tgUsername = user.tgUsername,
        address = dbQuery{user.address.address},
        password = user.password,
        salt = user.salt
    )

    override suspend fun createNewUser(userDTO: UserDTO): Boolean {
        return repository.createNewUser(userDTO)
    }

    override suspend fun getUserById(id: UUID): UserDTO? {
        val user = repository.getUserById(id) ?: return null
        return userToUserDTO(user)
    }

    override suspend fun getUserByUsername(username: String): UserDTO? {
        val user = repository.getUserByUsername(username) ?: return null
        return userToUserDTO(user)
    }

    override suspend fun updateUserPersonalInfo(id: UUID, request: PatchUserPersonalInfoRequest): Boolean {
        return repository.updateUserPersonalInfo(id, request)
    }

    override suspend fun updateUser(id: UUID, request: PatchUserByAdminRequest): Boolean {
        return repository.updateUser(id, request)
    }

    override suspend fun deleteUser(id: UUID) {
        repository.deleteUser(id)
    }

    override suspend fun getUsers(): List<UserDTO> {
        return repository.getUsers()
    }
}