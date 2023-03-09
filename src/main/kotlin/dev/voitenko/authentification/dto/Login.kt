package dev.voitenko.authentification.dto

import kotlinx.serialization.Serializable

@Serializable
class LoginBody(
    val id: String,
    val email: String,
    val password: String
)
@Serializable
class LoginResponse(
    val token: String,
)