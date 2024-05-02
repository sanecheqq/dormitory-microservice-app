package ru.missclick3.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.missclick3.external.UserService
import ru.missclick3.routes.adminBookingRoutes
import ru.missclick3.routes.bookingRoute
import ru.missclick3.services.TimeRangeService
import ru.missclick3.services.WashingMachineService

fun Application.configureRouting(
    userService: UserService,
    timeRangeService: TimeRangeService,
    washingMachineService: WashingMachineService
) {
    routing {
        bookingRoute(
            userService = userService,
            timeRangeService = timeRangeService,
            washingMachineService = washingMachineService
        )
        adminBookingRoutes(
            washingMachineService = washingMachineService,
            timeRangeService,
            userService
        )
    }
}
