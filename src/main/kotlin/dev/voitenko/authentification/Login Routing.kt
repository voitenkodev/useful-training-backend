package dev.voitenko.authentification

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureLoginRouting() {
    routing {
        post("/login") {
            val authController = AuthController(call)
            authController.loginUser()
        }
    }
}
