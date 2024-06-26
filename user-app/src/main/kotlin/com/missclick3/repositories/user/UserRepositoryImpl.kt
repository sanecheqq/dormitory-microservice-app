package com.missclick3.repositories.user

import com.missclick3.config.DatabaseSingleton.dbQuery
import com.missclick3.messages.dtos.UserDTO
import com.missclick3.messages.requests.PatchUserByAdminRequest
import com.missclick3.messages.requests.PatchUserPersonalInfoRequest
import com.missclick3.model.*
import com.missclick3.util.UserRole
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class UserRepositoryImpl : UserRepository {
    private fun userToUserDTO(user: User) = UserDTO(
        username = user.username,
        role = user.role.toString(),
        name = user.name,
        surname = user.surname,
        patronymic = user.patronymic,
        id = user.id.toString(),
        email = user.email,
        phoneNumber = user.phoneNumber,
        tgUsername = user.tgUsername,
        address = user.address.address,
        password = user.password,
        salt = user.salt
    )

    override suspend fun createNewUser(userDTO: UserDTO): Boolean = try {
        dbQuery {
            User.new {
                username = userDTO.username
                role = UserRole.valueOf(userDTO.role)
                name = userDTO.name
                surname = userDTO.surname
                patronymic = userDTO.patronymic
                address = DormitoryAddress.find { DormitoryAddressesTable.address eq userDTO.address }.singleOrNull()!!
                email = userDTO.email
                phoneNumber = userDTO.phoneNumber
                tgUsername = userDTO.tgUsername
                password = userDTO.password.toString()
                salt = userDTO.salt.toString()
            }
        }
        true
    } catch (e: Exception) {
        false
    }

    override suspend fun getUserById(id: UUID): User? {
        return dbQuery {
            User.findById(id)
        }
    }

    override suspend fun getUserByUsername(username: String): User? {
        return dbQuery {
            User.find { UsersTable.username eq username }.singleOrNull()
        }
    }

    override suspend fun updateUserPersonalInfo(id: UUID, request: PatchUserPersonalInfoRequest): Boolean {
        return try {
            dbQuery {
                val user = User.findById(id)
                user?.email = request.email
                user?.phoneNumber = request.phoneNumber
                user?.tgUsername = request.tgUsername
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun updateUser(id: UUID, request: PatchUserByAdminRequest): Boolean {
        return try {
            dbQuery {
                val user = User.findById(id)
                user?.role = UserRole.valueOf(request.role)
                user?.username = request.username
                user?.name = request.name
                user?.surname = request.surname
                user?.patronymic = request.patronymic
                user?.address = DormitoryAddress.find { DormitoryAddressesTable.address eq request.address }.singleOrNull()!!

                val fluroCertDTO = request.fluroCert
                val stdsCertDTO = request.stdsCert
                val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                if (fluroCertDTO != null) {
                    FluoroCertificate.find { FluoroCertificatesTable.userId eq id }.singleOrNull()?.delete()
                    FluoroCertificate.new {
                        startDate = LocalDate.parse(fluroCertDTO.startDate, formatter)
                        expireDate = LocalDate.parse(fluroCertDTO.expireDate, formatter)
                        this.user = user!!
                    }
                }
                if (stdsCertDTO != null) {
                    STDsCertificate.find {STDsCertificatesTable.userId eq id}.singleOrNull()?.delete()
                    STDsCertificate.new {
                        startDate = LocalDate.parse(stdsCertDTO.startDate, formatter)
                        expireDate = LocalDate.parse(stdsCertDTO.expireDate, formatter)
                        this.user = user!!
                    }
                }
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun getUsers(): List<UserDTO> {
        return dbQuery {
            User.all()
                .map(::userToUserDTO)
                .toList()
        }
    }

    override suspend fun deleteUser(id: UUID): Boolean {
        return try {
            dbQuery {
                val cert = FluoroCertificate.find { FluoroCertificatesTable.userId eq id }.singleOrNull()
                cert?.delete()
            }
            dbQuery {
                val cert = STDsCertificate.find { STDsCertificatesTable.userId eq id}.singleOrNull()
                cert?.delete()
            }
            dbQuery {
                val user = User.findById(id)
                user?.delete()
            }
            true
        } catch (e: Exception) {
            false
        }
    }
}