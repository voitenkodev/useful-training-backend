package dev.voitenko.services.authentification.dto

import kotlinx.serialization.Serializable

@Serializable
class LoginBody(
    val email: String,
    val password: String
)

@Serializable
class LoginResponse(
    val token: String,
)