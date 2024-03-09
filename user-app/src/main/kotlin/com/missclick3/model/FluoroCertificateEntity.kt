package com.missclick3.model

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.date
import java.util.*

object FluoroCertificatesTable: UUIDTable("fluoro_certificates") {
    val startDate = date("startDate")
    val expireDate = date("expireDate")

    val userId = reference("userId", UsersTable)
}

class FluoroCertificate(id: EntityID<UUID>): UUIDEntity(id) {
    companion object : UUIDEntityClass<FluoroCertificate>(FluoroCertificatesTable)

    var startDate by FluoroCertificatesTable.startDate
    var expireDate by FluoroCertificatesTable.expireDate
    var user by User referencedOn FluoroCertificatesTable.userId
}