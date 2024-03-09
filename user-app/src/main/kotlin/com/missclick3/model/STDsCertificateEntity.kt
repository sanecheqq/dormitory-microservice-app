package com.missclick3.model

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.date
import java.util.*

object STDsCertificatesTable: UUIDTable("stds_certificates") {
    val startDate = date("startDate")
    val expireDate = date("expireDate")

    val userId = reference("userId", UsersTable)
}

class STDsCertificate(id: EntityID<UUID>): UUIDEntity(id) {
    companion object : UUIDEntityClass<STDsCertificate>(STDsCertificatesTable)

    var startDate by STDsCertificatesTable.startDate
    var expireDate by STDsCertificatesTable.expireDate
    var user by User referencedOn STDsCertificatesTable.userId
}