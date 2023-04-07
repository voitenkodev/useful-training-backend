package dev.voitenko.services.trainings

import dev.voitenko.database.*
import dev.voitenko.services.trainings.dto.Training
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.and
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

        val id = call.parameters["id"]?.toLongOrNull()

        val training = Trainings.getTrainings {
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

        val trainings = Trainings.getTrainings {
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

        val trainingId = Trainings.insert(user.token, body).value.toString()

        call.respond(HttpStatusCode.OK, trainingId)
    }

    suspend fun removeTraining() {
        val token = call.request.headers["Authorization"]?.replace("Bearer ", "")
        val user = Users.getByToken(token = UUID.fromString(token))

        if (user == null) {
            call.respond(HttpStatusCode.Unauthorized, "User Not Found")
            return
        }

        val id = call.request.queryParameters["id"]?.toLongOrNull()

        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, "Invalid ID")
            return
        }

        Trainings.remove(trainingId = id)


        call.respond(HttpStatusCode.OK)
    }

    suspend fun getExercises() {
        val token = call.request.headers["Authorization"]?.replace("Bearer ", "")
        val user = Users.getByToken(token = UUID.fromString(token))

        if (user == null) {
            call.respond(HttpStatusCode.Unauthorized, "User Not Found")
            return
        }

        val name = call.request.queryParameters["name"] ?: ""

        val response = Trainings.getExercises {
            select { (Trainings.user_id eq user.token) and (Exercises.name like "$name%") }
        }

        call.respond(HttpStatusCode.OK, response)
    }
}