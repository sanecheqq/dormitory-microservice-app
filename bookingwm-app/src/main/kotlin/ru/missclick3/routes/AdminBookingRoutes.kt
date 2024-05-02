package ru.missclick3.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.missclick3.external.UserDTO
import ru.missclick3.external.UserService
import ru.missclick3.messages.requests.GetWMsForDormitoryRequest
import ru.missclick3.messages.requests.WashingMachineRequest
import ru.missclick3.messages.requests.WmNumRequest
import ru.missclick3.services.TimeRangeService
import ru.missclick3.services.WashingMachineService

fun Route.adminBookingRoutes(
    washingMachineService: WashingMachineService,
    timeRangeService: TimeRangeService,
    userService: UserService
) {
    route("booking/admin") {
        lateinit var authHeader: String
        lateinit var userDto: UserDTO
        intercept(ApplicationCallPipeline.Call) {
            val token = call.request.header(HttpHeaders.Authorization)

            if (token == null) {
                call.respond(HttpStatusCode.Unauthorized)
                return@intercept finish()
            }

            authHeader = token
            println("JOOOOOOOOOOOOOOPAAAAAAAAAAAAAAAA $authHeader")

            val user = userService.getUserDto(authHeader)
            println("USSSSSSER ${user?.role}")
            if (user == null || user.role != "ADMIN") {
                call.respond(HttpStatusCode.MethodNotAllowed, "Ты чмо, а не админ")
                return@intercept
            }
            userDto = user
        }
        get {
            val wms = washingMachineService.getWMsForDormitory(
                GetWMsForDormitoryRequest(userDto.address)
            )

            call.respond(HttpStatusCode.OK, wms)
        }
        post {
            val request = call.receiveNullable<WmNumRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            val added = washingMachineService.addWM(
                WashingMachineRequest(
                    address = userDto.address,
                    wmNumber = request.wmNumber
                )
            )

            if (!added) {
                call.respond(HttpStatusCode.Conflict, "WM was not added")
                return@post
            }

            call.respond(HttpStatusCode.OK, "WM was added")
        }
        patch {
            val request = call.receiveNullable<WmNumRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@patch
            }

            val statusChanged = washingMachineService.changeStatus(
                WashingMachineRequest(
                    address = userDto.address,
                    wmNumber = request.wmNumber
                )
            )

            if(!statusChanged) {
                call.respond(HttpStatusCode.Conflict, "Status was not changed")
                return@patch
            }

            call.respond(HttpStatusCode.OK, "Status changed")
        }
        delete {
            val request = call.receiveNullable<WmNumRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }

            val deleted = washingMachineService.deleteWM(
                WashingMachineRequest(
                    address = userDto.address,
                    wmNumber = request.wmNumber
                )
            )

            if (!deleted) {
                call.respond(HttpStatusCode.Conflict, "WM was not deleted")
                return@delete
            }

            call.respond(HttpStatusCode.OK, "WM was deleted")
        }
    }
}