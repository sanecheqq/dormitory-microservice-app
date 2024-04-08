package com.missclick3.routes

import com.missclick3.messages.dtos.UserDTO
import com.missclick3.messages.requests.PatchUserPersonalInfoRequest
import com.missclick3.messages.responses.UserInfoResponse
import com.missclick3.model.FluoroCertificate
import com.missclick3.model.STDsCertificate
import com.missclick3.services.certificates.CertificateService
import com.missclick3.services.saved_news.SavedNewsService
import com.missclick3.services.user.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.apache.commons.validator.routines.EmailValidator
import java.util.*

fun Route.authenticate() {
    authenticate("myAuth") {
        get("/authenticate") {
            call.respond(HttpStatusCode.OK)
        }
    }
}

fun Route.userRoutes(
    userService: UserService,
    fluoroService: CertificateService<FluoroCertificate>,
    stdsService: CertificateService<STDsCertificate>,
    savedNewsService: SavedNewsService
) {
    authenticate("myAuth") {
        route("/user") {
            get {
                val principal = call.principal<JWTPrincipal>()
                val userId = try {
                    UUID.fromString(principal?.getClaim("userId", String::class).toString())
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.Conflict)
                    return@get
                }

                val userDTO = userService.getUserById(userId)
                if (userDTO == null) {
                    call.respond(HttpStatusCode.Conflict, "invalid password")
                    return@get
                }
                val newUserDTO = UserDTO(
                    id = userDTO.id,
                    name = userDTO.name,
                    surname = userDTO.surname,
                    patronymic = userDTO.patronymic,
                    username = userDTO.username,
                    email = userDTO.email,
                    phoneNumber = userDTO.phoneNumber,
                    tgUsername = userDTO.tgUsername,
                    address = userDTO.address,
                    role = userDTO.role
                )
                val fluoroCertDTO = fluoroService.getCertificateByUserId(userId)
                val stdsCertificate = stdsService.getCertificateByUserId(userId)

                call.respond(
                    HttpStatusCode.OK,
                    UserInfoResponse(
                        userDTO = newUserDTO,
                        fluoroCertificateDTO = fluoroCertDTO,
                        stdsCertificateDTO = stdsCertificate
                    )
                )
            }

            patch {
                val principal = call.principal<JWTPrincipal>()
                val userId = try {
                    UUID.fromString(principal?.getClaim("userId", String::class).toString())
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.Conflict)
                    return@patch
                }

                val request = call.receiveNullable<PatchUserPersonalInfoRequest>() ?: kotlin.run {
                    call.respond(HttpStatusCode.BadRequest)
                    return@patch
                }

                val isEmailValid = EmailValidator.getInstance().isValid(request.email!!)
                        || request.email.isBlank()
                val isPhoneNumberValid = Regex("^\\+7\\d{10}$").matches(request.phoneNumber!!)
                        || request.phoneNumber.isBlank()
                val isTgUsernameValid = Regex("^@[a-zA-Z0-9_]{5,32}$").matches(request.tgUsername!!)
                        || request.tgUsername.isBlank()

//                if (!isEmailValid || !isTgUsernameValid || !isPhoneNumberValid) {
//                    call.respond(HttpStatusCode.Conflict, "Incorrect options")
//                    return@patch
//                }
                val map = hashMapOf<String, String>()
                if (!isEmailValid) {
                    map["email"] = "Некорректный email"
                }
                if (!isPhoneNumberValid) {
                    map["phoneNumber"] = "Некорректный номер телефона"
                }
                if (!isTgUsernameValid) {
                    map["tgUsername"] = "Некорректный логин telegram"
                }

                if (map.isNotEmpty()) {
                    call.respond(HttpStatusCode.Conflict, map)
                    return@patch
                }
                val updated = userService.updateUserPersonalInfo(userId, request)

                if (!updated) {
                    call.respond(HttpStatusCode.Conflict, "user was not updated")
                    return@patch
                }

                call.respond(HttpStatusCode.OK, "User was updated")
            }
        }
    }
}