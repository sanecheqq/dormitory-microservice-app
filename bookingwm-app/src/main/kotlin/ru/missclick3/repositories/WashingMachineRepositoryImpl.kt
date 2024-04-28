package ru.missclick3.repositories

import org.jetbrains.exposed.sql.and
import ru.missclick3.config.DatabaseSingleton.dbQuery
import ru.missclick3.messages.dtos.WashingMachineDto
import ru.missclick3.model.WashingMachine
import ru.missclick3.model.WashingMachineTable

class WashingMachineRepositoryImpl : WashingMachineRepository {
    private fun wmToWMDTO(wm: WashingMachine) : WashingMachineDto {
        return WashingMachineDto(
            id = wm.id.toString(),
            address = wm.dormitoryAddress,
            wmNumber = wm.wmNumber,
            enabled = wm.enabled
        )
    }

    override suspend fun changeStatus(
        address: String,
        wmNumber: Int
    ): Boolean {
        return try {
            dbQuery {
                val wm = WashingMachine.find {
                    (WashingMachineTable.dormitoryAddress eq address) and
                            (WashingMachineTable.wmNumber eq wmNumber)
                }.singleOrNull()
                if (wm != null) {
                    wm.enabled = !wm.enabled
                }
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun getWMsForDormitory(address: String): List<WashingMachineDto> {
        return dbQuery {
            WashingMachine.find { WashingMachineTable.dormitoryAddress eq address }
                .map(::wmToWMDTO)
        }
    }

    override suspend fun addWM(address: String, wmNumber: Int): Boolean {
        return try {
            var good = true
            dbQuery {
                val wm = WashingMachine.find {
                    (WashingMachineTable.dormitoryAddress eq address) and
                            (WashingMachineTable.wmNumber eq wmNumber)
                }.singleOrNull()
                if (wm != null) {
                    good = false
                }
                else {
                    WashingMachine.new {
                        this.wmNumber = wmNumber
                        dormitoryAddress = address
                        enabled = false
                    }
                }
            }
            good
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun deleteWM(address: String, wmNumber: Int): Boolean {
        return try {
            var good = true
            dbQuery {
                val wm = WashingMachine.find {
                    (WashingMachineTable.dormitoryAddress eq address) and
                            (WashingMachineTable.wmNumber eq wmNumber)
                }.singleOrNull()
                if (wm == null) {
                    good = false
                } else {
                    wm.delete()
                }
            }
            good
        } catch (e: Exception) {
            false
        }
    }
}