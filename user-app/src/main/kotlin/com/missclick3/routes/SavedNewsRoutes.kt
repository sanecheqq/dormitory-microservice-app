package com.missclick3.routes

import com.missclick3.messages.requests.AddSavedNewsRequest
import com.missclick3.routes.authenticate
import com.missclick3.services.saved_news.SavedNewsService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Route.savedNewsRoutes(
    savedNewsService: SavedNewsService
) {
    authenticate("myAuth") {
        route("/saved-news") {
            get() {
                val principal = call.principal<JWTPrincipal>()
                val userId = try {
                    UUID.fromString(principal?.getClaim("userId", String::class).toString())
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.Conflict)
                    return@get
                }

                call.respond(HttpStatusCode.OK, savedNewsService.getSavedNews(userId))
            }

            post() {
                val principal = call.principal<JWTPrincipal>()
                val userId = try {
                    UUID.fromString(principal?.getClaim("userId", String::class).toString())
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.Conflict)
                    return@post
                }

                val request = call.receiveNullable<AddSavedNewsRequest>() ?: kotlin.run {
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }

                val added = savedNewsService.addToSavedNews(UUID.fromString(request.newsId), userId)
                if (!added) {
                    call.respond(HttpStatusCode.Conflict, "Айди новости не добавлено")
                    return@post
                }

                call.respond(HttpStatusCode.OK, "Айди новости добавлено")
            }
            delete("/{id}") {
                val principal = call.principal<JWTPrincipal>()
                val userId = try {
                    UUID.fromString(principal?.getClaim("userId", String::class).toString())
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.Conflict)
                    return@delete
                }

                val newsId = call.parameters["id"] ?: return@delete call.respond(
                    HttpStatusCode.BadRequest,
                    "Id is missing"
                )

                val deleted = savedNewsService.deleteFromSavedNews(newsId, userId)

                if (!deleted) {
                    call.respond(HttpStatusCode.Conflict, "was not deleted from saved news")
                    return@delete
                }

                call.respond(HttpStatusCode.OK, "deleted from saved news")
            }
        }
    }
}