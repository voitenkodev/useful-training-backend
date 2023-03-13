package dev.voitenko.services.trainings

import dev.voitenko.database.*
import dev.voitenko.services.trainings.dto.Training
import io.ktor.http.*
import io.ktor.http.auth.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import java.util.*

class TrainingsController(private val call: ApplicationCall) {

    suspend fun getTraining() {
        val token = call.request.headers["Authorization"]?.replace("Bearer ", "")
        val user = Users.getByToken(token = UUID.fromString(token))

        if (user == null) {
            call.respond(HttpStatusCode.Unauthorized, "User Not Found")
            return
        }

        val id = UUID.fromString(call.parameters["id"])

        val training = Trainings.get {
            select { Trainings.id eq id }
        }.firstOrNull()

        if (training == null) {
            call.respond(HttpStatusCode.NotFound, "Training Not Found")
            return
        }

        call.respond(HttpStatusCode.OK, training)
    }

    suspend fun getTrainings() {
        val token = call.request.headers["Authorization"]?.replace("Bearer ", "")
        val user = Users.getByToken(token = UUID.fromString(token))

        if (user == null) {
            call.respond(HttpStatusCode.Unauthorized, "User Not Found")
            return
        }

        val trainings = Trainings.get {
            select { Trainings.user_id eq user.token }
        }

        call.respond(HttpStatusCode.OK, trainings)
    }

    suspend fun setTraining() {
        val token = call.request.headers["Authorization"]?.replace("Bearer ", "")
        val user = Users.getByToken(token = UUID.fromString(token))

        if (user == null) {
            call.respond(HttpStatusCode.Unauthorized, "User Not Found")
            return
        }

        val body = call.receive<Training>()

        Trainings.insert(user.token, body)

        call.respond(HttpStatusCode.OK)
    }
}