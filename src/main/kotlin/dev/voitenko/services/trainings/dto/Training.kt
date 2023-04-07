package dev.voitenko.services.trainings.dto

import kotlinx.serialization.Serializable

@Serializable
data class Training(
    val id: Long? = null,
    val exercises: List<Exercise> = emptyList(),
    val duration: String,
    val date: String,
    val tonnage: Double,
    val countOfLifting: Int,
    val intensity: Double,
)