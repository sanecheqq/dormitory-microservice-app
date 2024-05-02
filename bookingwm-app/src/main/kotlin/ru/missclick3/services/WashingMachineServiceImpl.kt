package ru.missclick3.services

import ru.missclick3.messages.dtos.WashingMachineDto
import ru.missclick3.messages.requests.GetWMsForDormitoryRequest
import ru.missclick3.messages.requests.WashingMachineRequest
import ru.missclick3.repositories.WashingMachineRepository

class WashingMachineServiceImpl(
    private val repository: WashingMachineRepository
) : WashingMachineService {
    override suspend fun changeStatus(request: WashingMachineRequest): Boolean {
        return repository.changeStatus(request.address, request.wmNumber)
    }

    override suspend fun getWMsForDormitory(request: GetWMsForDormitoryRequest): List<WashingMachineDto> {
        return repository.getWMsForDormitory(request.address)
    }

    override suspend fun getEnabledWMsForDormitory(request: GetWMsForDormitoryRequest): List<WashingMachineDto> {
        return repository.getEnabledWMsForDormitory(request.address)
    }

    override suspend fun addWM(request: WashingMachineRequest): Boolean {
        return repository.addWM(request.address, request.wmNumber)
    }

    override suspend fun deleteWM(request: WashingMachineRequest): Boolean {
        return repository.deleteWM(request.address, request.wmNumber)
    }
}