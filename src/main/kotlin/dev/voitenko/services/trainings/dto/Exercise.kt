package dev.voitenko.services.trainings.dto

import kotlinx.serialization.Serializable

@Serializable
data class Exercise(
    val id: Long? = null,
    val name: String,
    val iterations: List<Iteration> = emptyList(),
    val tonnage: Double,
    val countOfLifting: Int,
    val intensity: Double,
)