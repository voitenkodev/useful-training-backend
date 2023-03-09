package dev.voitenko.dto

import kotlinx.serialization.Serializable

@Serializable
data class ShortTraining(
    val id: String,
    val exercises: List<String> = emptyList(),
    val duration: String,
    val date: String,
)