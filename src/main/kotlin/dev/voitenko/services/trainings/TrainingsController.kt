package dev.voitenko.services.trainings

import dev.voitenko.database.*
import dev.voitenko.services.trainings.dto.Training
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.jetbrains.exposed.sql.ResultRow
import java.util.*

class TrainingsController(private val call: ApplicationCall) {

    suspend fun getTraining() {

        val id = UUID.fromString(call.parameters["id"])

        val training= Trainings.get(id).firstOrNull()

        if (training == null) {
            call.respond(HttpStatusCode.BadRequest, "Training Not Found")
            return
        }

        println(training)

        call.respond(HttpStatusCode.OK, training)
    }

    suspend fun setTraining() {

        val body = call.receive<Training>()

        Trainings.insert(body)

        call.respond(HttpStatusCode.OK)
    }
}