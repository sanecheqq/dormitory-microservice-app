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
            }
        }
    }
}