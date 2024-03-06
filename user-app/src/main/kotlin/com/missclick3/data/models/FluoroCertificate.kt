package com.missclick3.data.models

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.timestamp

data class FluoroCertificate(
    val id: String? = "",
    val startDate: String,
    val expireDate: String
)

object FluoroCertificates: UUIDTable("fluorocertificates") {
    val startDate = date("startDate")
    val expireDate = date("expireDate")

    val userId = reference("userId", Users)
}