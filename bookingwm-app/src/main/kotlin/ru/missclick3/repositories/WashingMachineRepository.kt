package ru.missclick3.repositories

import ru.missclick3.messages.dtos.WashingMachineDto

interface WashingMachineRepository {
    suspend fun changeStatus(
        address: String,
        wmNumber: Int
    ) : Boolean
    suspend fun getWMsForDormitory(
        address: String
    ) : List<WashingMachineDto>
    suspend fun addWM(
        address: String,
        wmNumber: Int
    ) : Boolean
    suspend fun deleteWM(
        address: String,
        wmNumber: Int
    ) : Boolean
}