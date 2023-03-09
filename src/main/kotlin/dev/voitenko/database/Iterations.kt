package dev.voitenko.database

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
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

    fun getAll(exercise_id: UUID): List<IterationsDto> = transaction {
        Iterations
            .select { Iterations.exercise_id eq exercise_id }
            .map { it.toDto() }
    }

    private fun ResultRow.toDto(): IterationsDto = IterationsDto(
        id = this[Iterations.id].value,
        exercise_id = this[exercise_id],
        weight = this[weight],
        repeat = this[repeat],
    )
}