package ru.missclick3.model

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.*

object WashingMachineTable : UUIDTable("washing_machine") {
    val wmNumber = integer("wmNumber")
    val enabled = bool("enabled")
    val dormitoryAddress = varchar("dormitoryAddress", 512)
}

class WashingMachine(id: EntityID<UUID>) :  UUIDEntity(id) {
    companion object : UUIDEntityClass<WashingMachine>(WashingMachineTable)

    var wmNumber by WashingMachineTable.wmNumber
    var enabled by WashingMachineTable.enabled
    var dormitoryAddress by WashingMachineTable.dormitoryAddress
}