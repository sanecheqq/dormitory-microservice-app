package com.missclick3.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.timestamp

@Serializable
data class Certificate(
    val id: String? = "",
    val fluraStartDate: String,
    val fluraExpireDate: String,
    val zppStartDate: String,
    val zppExpireDate: String
)

object Certificates: UUIDTable("certificates") {
    val fluraStartDate = timestamp("fluraStartDate")
    val fluraExpireDate = timestamp("fluraExpireDate")
    val zppStartDate = timestamp("zppStartDate")
    val zppExpireDate = timestamp("zppExpireDate")
    val userId = reference("userId", Users)
}
