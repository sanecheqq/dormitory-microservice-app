package ru.missclick3.repositories

import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.or
import ru.missclick3.config.DatabaseSingleton.dbQuery
import ru.missclick3.messages.dtos.TimeRangeDto
import ru.missclick3.model.TimeRange
import ru.missclick3.model.TimeRangeTable
import ru.missclick3.model.WashingMachine
import ru.missclick3.model.WashingMachineTable
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class TimeRangeRepositoryImpl : TimeRangeRepository {
    private fun parseToLocalDateTimeFromString(time: String, date: String) : LocalDateTime {
        val dateParts = date.split("-").map { it.toInt() }
        val timeParts = time.split("-").map {it.toInt()}
        return LocalDateTime.of(dateParts[2], dateParts[1], dateParts[0], timeParts[0], timeParts[1])
            .atZone(ZoneId.of("Europe/Moscow"))
            .toLocalDateTime()
    }

    private fun timeRangeToTimeRangeDto(timeRange: TimeRange) : TimeRangeDto {
        return TimeRangeDto(
            id = timeRange.id.toString(),
            startTime = timeRange.startTime.toString(),
            endTime = timeRange.endTime.toString(),
            date = timeRange.date.toString(),
            userId = timeRange.userId,
            userTg = timeRange.userTg,
            wmNumber = timeRange.washingMachine.wmNumber,
            withDrier = timeRange.withDrier
        )
    }
    private fun parseToLocalDateFromString(dateString: String) : LocalDate {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val date = LocalDate.parse(dateString, formatter)
        val moscowZoneId = ZoneId.of("Europe/Moscow")
        return date.atStartOfDay(moscowZoneId).toLocalDate()
    }

    override suspend fun bookTimeRange(
        address: String,
        wmNumber: Int,
        userId: String,
        userTg: String,
        startTime: String,
        endTime: String,
        date: String,
        withDrier: Boolean
    ): Boolean {
        return try {
            var good = true
            dbQuery {
                val wm = WashingMachine.find {
                    WashingMachineTable.dormitoryAddress eq address and
                            (WashingMachineTable.wmNumber eq wmNumber)
                }.singleOrNull()
                if (wm != null) {
                    val wmId = wm.id
                    val toAddStartTime = parseToLocalDateTimeFromString(startTime, date)
                    val toAddEndTime = parseToLocalDateTimeFromString(endTime, date)
                    val toAddDate = parseToLocalDateFromString(date)
                    val timeRange = TimeRange.find {
                        TimeRangeTable.startTime eq toAddStartTime and
                                (TimeRangeTable.endTime eq toAddEndTime) and
                                (TimeRangeTable.date eq toAddDate) and
                                (TimeRangeTable.washingMachine eq wmId)
                    }.singleOrNull()
                    if (timeRange == null) {
                        TimeRange.new {
                            this.startTime = toAddStartTime
                            this.endTime = toAddEndTime
                            this.date = toAddDate
                            this.washingMachine = wm
                            this.userId = userId
                            this.userTg = userTg
                            this.withDrier = withDrier
                        }
                    }
                    else {
                        good = false
                    }
                } else {
                    good = false
                }
            }
            good
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun deleteBooking(
        address: String,
        wmNumber: Int,
        startTime: String,
        endTime: String,
        date: String
    ): Boolean {
        return try {
            var good = true
            dbQuery {
                val wm = WashingMachine.find {
                    WashingMachineTable.dormitoryAddress eq address and
                            (WashingMachineTable.wmNumber eq wmNumber)
                }.singleOrNull()
                if (wm != null) {
                    val wmId = wm.id
                    val toAddStartTime = parseToLocalDateTimeFromString(startTime, date)
                    val toAddEndTime = parseToLocalDateTimeFromString(endTime, date)
                    val toAddDate = parseToLocalDateFromString(date)
                    val timeRange = TimeRange.find {
                        TimeRangeTable.startTime eq toAddStartTime and
                                (TimeRangeTable.endTime eq toAddEndTime) and
                                (TimeRangeTable.date eq toAddDate) and
                                (TimeRangeTable.washingMachine eq wmId)
                    }.singleOrNull()
                    if (timeRange != null) {
                        timeRange.delete()
                    } else {
                        good = false
                    }
                } else {
                    good = false
                }
            }
            good
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun getTimeRange(
        address: String,
        wmNumber: Int,
        startTime: String,
        endTime: String,
        date: String
    ): TimeRangeDto? {
        return dbQuery {
            val wm = WashingMachine.find {
                WashingMachineTable.dormitoryAddress eq address and
                        (WashingMachineTable.wmNumber eq wmNumber)
            }.singleOrNull()
            if (wm != null) {
                TimeRange.find {
                    TimeRangeTable.startTime eq parseToLocalDateTimeFromString(startTime, date) and
                            (TimeRangeTable.endTime eq parseToLocalDateTimeFromString(endTime, date)) and
                            (TimeRangeTable.date eq parseToLocalDateFromString(date)) and
                            (TimeRangeTable.washingMachine eq wm.id)
                }.map(::timeRangeToTimeRangeDto).singleOrNull()
            } else {
                null
            }
        }
    }

    override suspend fun getTimeRangesForWm(address: String, wmNumber: Int, date: String): List<TimeRangeDto> {
        return dbQuery {
            val wm = WashingMachine.find {
                WashingMachineTable.dormitoryAddress eq address and
                        (WashingMachineTable.wmNumber eq wmNumber)
            }.singleOrNull()
            if (wm != null) {
                TimeRange.find {
                    (TimeRangeTable.date eq parseToLocalDateFromString(date)) and
                    (TimeRangeTable.washingMachine eq wm.id)
                }.map(::timeRangeToTimeRangeDto)
            } else {
                emptyList()
            }
        }
    }

    override suspend fun getTimeRangesForUser(userId: String): List<TimeRangeDto> {
        val date = LocalDate.now()
        val moscowZoneId = ZoneId.of("Europe/Moscow")
        val currentMoscowDate = date.atStartOfDay(moscowZoneId).toLocalDate()
        return dbQuery {
            TimeRange.find {
                (TimeRangeTable.userId eq userId) and
                        (TimeRangeTable.date greaterEq currentMoscowDate)
            }.map(::timeRangeToTimeRangeDto)
        }
    }

    override suspend fun getCurrentKeyUsers(address: String): List<String> {
        return dbQuery {
            val wmList = WashingMachine.find {
                WashingMachineTable.dormitoryAddress eq address
            }
            val timeNow = LocalDateTime.now()
                .atZone(ZoneId.of("Europe/Moscow"))
                .toLocalDateTime()
            val res = ArrayList<String>()
            wmList.forEach { res.addAll(
                TimeRange.find {
                    (TimeRangeTable.washingMachine eq it.id) and
                            (((TimeRangeTable.startTime lessEq  timeNow) and
                            (TimeRangeTable.endTime greaterEq timeNow )) or
                                    ((TimeRangeTable.withDrier eq true) and
                                    (TimeRangeTable.startTime lessEq  timeNow.minusHours(2)) and
                                    (TimeRangeTable.endTime greaterEq  timeNow.minusHours(2))))
                }.map { it.userId }
            ) }
            res
        }
    }

    override suspend fun getAmountOfTimeRangersForUser(userId: String) : Int {
        return getTimeRangesForUser(userId).size
    }
}