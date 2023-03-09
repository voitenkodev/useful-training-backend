package dev.voitenko.authentification

import dev.voitenko.dto.User
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureUserRouting() {
    routing {
        get("/user") {
            call.respond(
                User(
                    uid = "1234567890",
                    displayName = "MaxVoitenko",
                    email = "voitenko.dev@gmail.com"
                )
            )
        }
    }
}