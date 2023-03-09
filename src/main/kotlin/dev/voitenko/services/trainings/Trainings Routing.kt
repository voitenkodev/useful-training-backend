package dev.voitenko.services.trainings

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.trainingsRouting() {

    routing {
        get("/training") {
            val trainingsController = TrainingsController(call)
        }
    }

    routing {
        get("/training/{id}") {
            val trainingsController = TrainingsController(call)
        }
    }
}
