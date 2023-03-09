package dev.voitenko.authentification.dto

import kotlinx.serialization.Serializable

@Serializable
class RegisterBody(
    val email: String,
    val password: String
)
@Serializable
class RegisterResponse(
    val token: String,
)