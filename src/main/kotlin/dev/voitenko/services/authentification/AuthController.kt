package dev.voitenko.services.authentification

import dev.voitenko.database.UserDTO
import dev.voitenko.database.Users
import dev.voitenko.services.authentification.dto.LoginBody
import dev.voitenko.services.authentification.dto.LoginResponse
import dev.voitenko.services.authentification.dto.RegisterBody
import dev.voitenko.services.authentification.dto.RegisterResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import java.util.*

class AuthController(private val call: ApplicationCall) {

    suspend fun registerUser() {
        val body = call.receive<RegisterBody>()
        val isUserExist = Users.getByEmail(body.email) != null

        if (isUserExist) {
            call.respond(HttpStatusCode.Conflict, "User Already Exist")
        } else {
            val token = UUID.randomUUID()
            Users.insert(UserDTO(token = token, email = body.email, password = body.password))
            call.respond(RegisterResponse(token = token.toString()))
        }
    }

    suspend fun loginUser() {
        val body = call.receive<LoginBody>()
        val user = Users.getByEmail(body.email)
        if (user == null) {
            call.respond(HttpStatusCode.Conflict, "User Not Found")
        } else if (body.password != user.password) {
            call.respond(HttpStatusCode.Conflict, "Incorrect Password")
        } else {
            call.respond(LoginResponse(token = user.token.toString()))
        }
    }
}