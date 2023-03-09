package dev.voitenko.authentification

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRegisterRouting() {
    routing {
        post("/register") {
            val authController = AuthController(call)
            authController.registerUser()
        }
    }
}
