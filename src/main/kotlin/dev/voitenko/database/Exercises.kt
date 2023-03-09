package dev.voitenko.database

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

data class IterationsDto(
    val id: UUID,
    val exercise_id: UUID,
    val weight: Double,
    val repeat: Int,
)

object Iterations : UUIDTable(name = "iterations") {

    private val exercise_id = uuid("exercise_id").references(Exercises.id)
    private val weight = double("weight")
    private val repeat = integer("repeat")

    fun insert(dto: IterationsDto) {
        transaction {
            insert {
                it[id] = dto.id
                it[exercise_id] = dto.exercise_id
                it[weight] = dto.weight
                it[repeat] = dto.repeat
            }
        }
    }
}