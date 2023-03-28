package dev.voitenko.services.trainings

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.trainingsRouting() {
    routing {
        get("/training/{id}") {
            val trainingsController = TrainingsController(call)
            trainingsController.getTraining()
        }
    }
    routing {
        get("/trainings") {
            val trainingsController = TrainingsController(call)
            trainingsController.getTrainings()
        }
    }
   routing {
        get("/exercises") {
            val trainingsController = TrainingsController(call)
            trainingsController.getExercises()
        }
    }
    routing {
        post("/training") {
            val trainingsController = TrainingsController(call)
            trainingsController.setTraining()
        }
    }
   routing {
        delete("/training") {
            val trainingsController = TrainingsController(call)
            trainingsController.removeTraining()
        }
    }
}
