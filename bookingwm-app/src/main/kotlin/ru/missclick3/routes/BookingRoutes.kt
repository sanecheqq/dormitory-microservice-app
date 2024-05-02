package ru.missclick3.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.missclick3.external.UserService
import ru.missclick3.messages.requests.*
import ru.missclick3.messages.responses.GetBookingInfoForDormitory
import ru.missclick3.services.TimeRangeService
import ru.missclick3.services.WashingMachineService
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter


fun localDateToString(date: LocalDate) : String {
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    return date.format(formatter)
}
fun Route.bookingRoute(
    userService: UserService,
    timeRangeService: TimeRangeService,
    washingMachineService: WashingMachineService
) {
    route("booking") {

        get {
            val authHeader = call.request.header(HttpHeaders.Authorization)
            if (authHeader == null) {
                call.respond(HttpStatusCode.Unauthorized)
                return@get
            }

            val userDto = userService.getUserDto(authHeader)
            if (userDto == null) {
                call.respond(HttpStatusCode.Conflict, "There is no such user")
                return@get
            }

            val address = userDto.address

            val wms = washingMachineService.getEnabledWMsForDormitory(
                GetWMsForDormitoryRequest(address = address)
            ).map { it.wmNumber }

            val response = ArrayList<GetBookingInfoForDormitory>()
            wms.forEach {
                val today = localDateToString(
                    LocalDate.now().atStartOfDay(
                        ZoneId.of("Europe/Moscow")
                    ).toLocalDate()
                )
                val tomorrow = localDateToString(
                    LocalDate.now().atStartOfDay(
                        ZoneId.of("Europe/Moscow")
                    ).toLocalDate().plusDays(1)
                )
                response.add(
                    GetBookingInfoForDormitory(
                        date = today,
                        wmNumber = it,
                        timeRangesForToday = timeRangeService.getTimeRangesForWm(
                            GetTimeRangesForWmRequest(
                                address, it, today
                            )
                        ),
                        timeRangesForTomorrow = timeRangeService.getTimeRangesForWm(
                            GetTimeRangesForWmRequest(
                                address, it, tomorrow
                            )
                        )
                    )
                )
            }

            call.respond(HttpStatusCode.OK, response)
        }

        post {
            val authHeader = call.request.header(HttpHeaders.Authorization)
            if (authHeader == null) {
                call.respond(HttpStatusCode.Unauthorized)
                return@post
            }

            val request = call.receiveNullable<TimeRangeRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            val userDto = userService.getUserDto(authHeader)

            if (userDto == null) {
                call.respond(HttpStatusCode.Conflict, "No such user")
                return@post
            }

            val timeRange = timeRangeService.getTimeRange(
                GetTimeRangeRequest(
                    address = userDto.address,
                    wmNumber = request.wmNumber,
                    startTime = request.startTime,
                    endTime = request.endTime,
                    date = request.date
                )
            )

            if (timeRange != null) {
                call.respond(HttpStatusCode.Conflict, "Time range already booked")
                return@post
            }

            val booked = timeRangeService.bookTimeRange(
                BookTimeRangeRequest(
                    address = userDto.address,
                    wmNumber = request.wmNumber,
                    userId = userDto.id!!,
                    userTg = userDto.tgUsername!!,
                    startTime = request.startTime,
                    endTime = request.endTime,
                    date = request.date,
                    withDrier = request.withDrier
                )
            )

            if (!booked) {
                call.respond(HttpStatusCode.Conflict, "Booking was not successful")
                return@post
            }

            call.respond(HttpStatusCode.OK, "Time range was booked")
        }

        get("{id}") {
            val authHeader = call.request.header(HttpHeaders.Authorization)
            if (authHeader == null) {
                call.respond(HttpStatusCode.Unauthorized)
                return@get
            }

            val id = call.parameters["id"] ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                "Id is missing"
            )

            val userDto = userService.getUserDto(authHeader)
            if (userDto == null) {
                call.respond(HttpStatusCode.Conflict, "No such user")
                return@get
            }

            val timeRange = timeRangeService.getTimeRangeById(
                GetTimeRangeById(id)
            )

            if (timeRange == null) {
                call.respond(HttpStatusCode.Conflict, "No such time range")
                return@get
            }

            call.respond(HttpStatusCode.OK, timeRange)
        }

        delete("{id}") {
            val authHeader = call.request.header(HttpHeaders.Authorization)
            if (authHeader == null) {
                call.respond(HttpStatusCode.Unauthorized)
                return@delete
            }

            val id = call.parameters["id"] ?: return@delete call.respond(
                HttpStatusCode.BadRequest,
                "Id is missing"
            )

            val userDto = userService.getUserDto(authHeader)
            if (userDto == null) {
                call.respond(HttpStatusCode.Conflict, "No such user")
                return@delete
            }

            val userId = userDto.id!!
            val userRole = userDto.role

            val timeRange = timeRangeService.getTimeRangeById(
                GetTimeRangeById(id)
            )

            if (timeRange == null) {
                call.respond(HttpStatusCode.Conflict, "No such time range")
                return@delete
            }

            if (userRole == "STUDENT" && userId != timeRange.userId ) {
                call.respond(HttpStatusCode.MethodNotAllowed)
                return@delete
            }

            val deleted = timeRangeService.deleteBooking(
                GetTimeRangeById(id)
            )

            if (!deleted) {
                call.respond(HttpStatusCode.Conflict, "booking wasnt deleted")
                return@delete
            }

            call.respond(HttpStatusCode.OK, "Booking was deleted")
        }
    }
}