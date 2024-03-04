package com.missclick3.data.dao.user

import com.missclick3.data.dao.DatabaseSingleton.dbQuery
import com.missclick3.data.models.User
import com.missclick3.data.models.Users
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.*

class UserDaoImpl : UserDao {
    private fun resultRowToUser(row: ResultRow) = User (
        id = row[Users.id].toString(),
        username = row[Users.username],
        name = row[Users.name],
        surname = row[Users.surname],
        patronymic = row[Users.patronymic],
        email = row[Users.email],
        phoneNumber = row[Users.phoneNumber],
        tgUsername = row[Users.phoneNumber],
        address = row[Users.address],
        password = row[Users.password],
        salt = row[Users.salt]
    )

    override suspend fun insertNewUser(user: User): Boolean {
        return try {
            dbQuery {
                Users.insert { row ->
                    row[username] = user.username
                    row[name] = user.name
                    row[surname] = user.surname
                    row[patronymic] = user.patronymic
                    row[address] = user.address
                    row[email] = user.email
                    row[phoneNumber] = user.phoneNumber
                    row[tgUsername] = user.tgUsername
                    row[password] = user.password
                    row[salt] = user.salt
                }
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun getUserByUsername(username: String): User? = dbQuery {
        Users
            .selectAll()
            .where { Users.username eq username}
            .map(::resultRowToUser)
            .singleOrNull()
    }

    override suspend fun getUserById(id: UUID) : User? = dbQuery {
        Users
            .selectAll()
            .where {Users.id eq id}
            .map(::resultRowToUser)
            .singleOrNull()
    }


    override suspend fun updateUser(user: User): Boolean {
        return try {
            dbQuery {
                Users.update({ Users.username eq user.username }) {row ->
                    row[username] = user.username
                    row[name] = user.name
                    row[surname] = user.surname
                    row[patronymic] = user.patronymic
                    row[address] = user.address
                    row[email] = user.email
                    row[phoneNumber] = user.phoneNumber
                    row[tgUsername] = user.tgUsername
                    row[password] = user.password
                    row[salt] = user.salt
                }
                true
            }
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun deleteUser(username: String) {
        dbQuery {
            Users.deleteWhere { Users.id eq id }
        }
    }
}