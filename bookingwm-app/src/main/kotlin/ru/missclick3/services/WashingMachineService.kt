package ru.missclick3.services

import ru.missclick3.messages.dtos.WashingMachineDto
import ru.missclick3.messages.requests.GetWMsForDormitoryRequest
import ru.missclick3.messages.requests.WashingMachineRequest

interface WashingMachineService {
    suspend fun changeStatus(
        request: WashingMachineRequest
    ) : Boolean
    suspend fun getWMsForDormitory(
        request: GetWMsForDormitoryRequest
    ) : List<WashingMachineDto>

    suspend fun getEnabledWMsForDormitory(
        request: GetWMsForDormitoryRequest
    ) : List<WashingMachineDto>
    suspend fun addWM(
        request: WashingMachineRequest
    ) : Boolean
    suspend fun deleteWM(
        request: WashingMachineRequest
    ) : Boolean
}