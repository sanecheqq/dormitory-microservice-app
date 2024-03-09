package com.missclick3.model

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.*

object DormitoryAddressesTable: UUIDTable("dormitory_addresses") {
    val address = varchar("address", 512).uniqueIndex()
}

class DormitoryAddress(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object: UUIDEntityClass<DormitoryAddress>(DormitoryAddressesTable)

    var address by DormitoryAddressesTable.address
}