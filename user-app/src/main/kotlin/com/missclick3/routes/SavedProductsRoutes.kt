package com.missclick3.routes

import com.missclick3.messages.requests.DeleteReferencesForSavedProduct
import com.missclick3.messages.requests.SavedProductRequest
import com.missclick3.routes.authenticate
import com.missclick3.services.saved_product.SavedProductService
import com.missclick3.services.user.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Route.savedProductsRoutes(
    savedProductService: SavedProductService,
    userService: UserService
) {
    lateinit var userId: UUID
    lateinit var userRole: String
    authenticate("myAuth") {
        route("/saved_products") {
            intercept(ApplicationCallPipeline.Call) {
                val principal = call.principal<JWTPrincipal>()
                userId = try {
                    UUID.fromString(principal?.getClaim("userId", String::class).toString())
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.Conflict)
                    return@intercept finish()
                }

                val user = userService.getUserById(userId)
                if (user == null) {
                    call.respond(HttpStatusCode.Conflict, "No user with that ID")
                    return@intercept finish()
                }

                userRole = user.role
            }

            get() {
                call.respond(HttpStatusCode.OK, savedProductService.getSavedProducts(userId))
            }

            post {
                val request = call.receiveNullable<SavedProductRequest>() ?: kotlin.run {
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }

                val added = savedProductService.addToSavedProducts(UUID.fromString(request.productId), userId)

                if (!added) {
                    call.respond(HttpStatusCode.Conflict, "Product was not added to Saved")
                    return@post
                }

                call.respond(HttpStatusCode.OK, "Product was added to Saved")
            }

            delete("/{id}") {
                val productId = call.parameters["id"] ?: kotlin.run {
                    call.respond(
                        HttpStatusCode.BadRequest, "Id is missing"
                    )
                    return@delete
                }

                val deleted = savedProductService.deleteFromSavedProducts(productId, userId)

                if (!deleted) {
                    call.respond(HttpStatusCode.Conflict, "Product was not deleted from saved")
                    return@delete
                }

                call.respond(HttpStatusCode.OK, "Product was deleted from saved")
            }

            delete("/followers") {
                val request = call.receiveNullable<DeleteReferencesForSavedProduct>() ?: kotlin.run {
                    call.respond(HttpStatusCode.BadRequest)
                    return@delete
                }
                val deleteProductUserId = request.deletedProductUserId

                if (userRole != "ADMIN" && userId != UUID.fromString(deleteProductUserId)) {
                    call.respond(HttpStatusCode.MethodNotAllowed, "ТЫ ЧМО, а не Админ")
                    return@delete
                }

                val deleted = savedProductService.deleteProductReferenceForAllUsers(request.productId)

                if (!deleted) {
                    call.respond(HttpStatusCode.Conflict, "Product was not deleted")
                    return@delete
                }

                call.respond(HttpStatusCode.OK, "Product was deleted")
            }
        }
    }
}