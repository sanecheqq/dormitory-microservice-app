package ru.missclick3.model

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.datetime
import java.util.*

object TimeRangeTable: UUIDTable("time_range") {
    val startTime = datetime("startDate")
    val endTime = datetime("endDate")
    val userId = varchar("userId", 128)
    val userTg = varchar("userTg", 128)
    val date = date("date")
    val withDrier = bool("withDrier")
    val status = bool("status")

    val washingMachine = reference("washingMachine", WashingMachineTable)
}

class TimeRange(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<TimeRange>(TimeRangeTable)

    var startTime by TimeRangeTable.startTime
    var endTime by TimeRangeTable.endTime
    var userId by TimeRangeTable.userId
    var userTg by TimeRangeTable.userTg
    var date by TimeRangeTable.date
    var withDrier by TimeRangeTable.withDrier
    var status by TimeRangeTable.status

    var washingMachine by WashingMachine referencedOn TimeRangeTable.washingMachine
}