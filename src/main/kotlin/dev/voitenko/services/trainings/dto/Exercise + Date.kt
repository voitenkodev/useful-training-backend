package dev.voitenko.services.trainings.dto

import kotlinx.serialization.Serializable

@Serializable
data class ExerciseDate(
    val trainingId: String? = null,
    val exercise: Exercise? = null,
    val date: String,
)