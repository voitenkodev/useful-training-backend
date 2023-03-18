package dev.voitenko

import dev.voitenko.database.DatabaseFactory
import dev.voitenko.plugins.configureSerialization
import dev.voitenko.services.authentification.authRouting
import dev.voitenko.services.trainings.trainingsRouting
import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    DatabaseFactory.init(environment.config)
    configureSerialization()
    testRouting()
    authRouting()
    trainingsRouting()
}

fun Application.testRouting() {

    routing {
        get("/test") {
            call.respond("Success get /test")

        }
        post("/test") {
            call.respond("Success post /test")

        }
    }
}