package ru.missclick3.routes

import io.ktor.server.routing.*
import ru.missclick3.external.UserService
import ru.missclick3.services.TimeRangeService
import ru.missclick3.services.WashingMachineService

fun Route.bookingRoute(
    userService: UserService,
    timeRangeService: TimeRangeService,
    washingMachineService: WashingMachineService
) {

}