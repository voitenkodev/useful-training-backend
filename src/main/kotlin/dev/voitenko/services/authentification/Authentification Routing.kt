package dev.voitenko.services.authentification

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.authRouting() {

    routing {
        post("/register") {
            val authController = AuthController(call)
            authController.registerUser()
        }
    }

    routing {
        post("/login") {
            val authController = AuthController(call)
            authController.loginUser()
        }
    }
}
