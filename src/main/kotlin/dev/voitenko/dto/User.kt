package dev.voitenko.dto

import kotlinx.serialization.Serializable

@Serializable
class User(
    val uid: String,
    val displayName: String,
    val email: String
)