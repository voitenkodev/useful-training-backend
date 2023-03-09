package dev.voitenko.services.authentification

import dev.voitenko.dto.User
import io.ktor.server.application.*
import io.ktor.server.response.*
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
