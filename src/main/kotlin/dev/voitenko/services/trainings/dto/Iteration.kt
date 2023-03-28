package dev.voitenko.services.trainings.dto

import kotlinx.serialization.Serializable

@Serializable
data class Iteration(
    val id: String? = null,
    val weight: Double,
    val repeat: Int
)