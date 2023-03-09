package dev.voitenko.services.trainings.dto

import kotlinx.serialization.Serializable

@Serializable
data class Training(
    val id: String,
    val exercises: List<Exercise> = emptyList(),
    val duration: String,
    val date: String,

    val tonnage: Double,
    val countOfLifting: Int,
    val intensity: Double,
) {

    @Serializable
    data class Exercise(
        val id: String,
        val name: String,
        val iterations: List<Iteration> = emptyList(),

        val tonnage: Double,
        val countOfLifting: Int,
        val intensity: Double,
    ) {
        @Serializable
        data class Iteration(
            val weight: Double,
            val repeat: Int
        )
    }
}