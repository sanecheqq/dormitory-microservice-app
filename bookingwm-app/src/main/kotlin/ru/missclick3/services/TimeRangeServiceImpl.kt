package ru.missclick3.services

import ru.missclick3.messages.dtos.TimeRangeDto
import ru.missclick3.messages.requests.*
import ru.missclick3.repositories.TimeRangeRepository

class TimeRangeServiceImpl(
    private val repository: TimeRangeRepository
) : TimeRangeService{
    override suspend fun bookTimeRange(request: BookTimeRangeRequest): Boolean {
        return repository.bookTimeRange(
            address = request.address,
            wmNumber = request.wmNumber,
            userId = request.userId,
            userTg = request.userTg,
            startTime = request.startTime,
            endTime = request.endTime,
            date = request.date,
            withDrier = request.withDrier
        )
    }

    override suspend fun deleteBooking(request: GetTimeRangeById): Boolean {
        return repository.deleteBooking(request.id)
    }

    override suspend fun getTimeRange(request: GetTimeRangeRequest): TimeRangeDto? {
        return repository.getTimeRange(
            address = request.address,
            wmNumber = request.wmNumber,
            startTime = request.startTime,
            endTime = request.endTime,
            date = request.date
        )
    }

    override suspend fun getTimeRangesForWm(request: GetTimeRangesForWmRequest): List<TimeRangeDto> {
        return repository.getTimeRangesForWm(
            address = request.address,
            wmNumber = request.wmNumber,
            date = request.date
        )
    }

    override suspend fun getTimeRangesForUser(request: GetTimeRangesForUserRequest): List<TimeRangeDto> {
        return repository.getTimeRangesForUser(
            userId = request.userId,
            date = request.date
        )
    }

    override suspend fun getCurrentKeyUsers(request: GetWMsForDormitoryRequest): List<String> {
        return repository.getCurrentKeyUsers(request.address)
    }

    override suspend fun getTimeRangeById(request: GetTimeRangeById): TimeRangeDto? {
        return repository.getTimeRangeById(request.id)
    }
}