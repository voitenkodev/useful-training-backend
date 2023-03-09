package dev.voitenko

import dev.voitenko.database.*
import dev.voitenko.plugins.configureSerialization
import dev.voitenko.services.authentification.authRouting
import dev.voitenko.services.trainings.trainingsRouting
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {
    Database.connect(
        url = "jdbc:postgresql://localhost:5432/usefultraining",
        driver = "org.postgresql.Driver",
        password = "37373",
        user = "postgres",
    )

    transaction {
        SchemaUtils
            .create(Users, Tokens, Trainings, Exercises, Iterations)
    }

    embeddedServer(
        factory = CIO,
        port = 8080,
        host = "0.0.0.0",
        module = Application::module
    ).start(wait = true)
}

fun Application.module() {
    configureSerialization()
    authRouting()
    trainingsRouting()
}