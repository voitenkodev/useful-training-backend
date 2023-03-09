package dev.voitenko.database

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

data class ExercisesDto(
    val id: UUID,
    val training_id: UUID,
    val name: String,
    val tonnage: Double,
    val countOfLifting: Int,
    val intensity: Double,
)

object Exercises : UUIDTable(name = "exercises") {

    private val training_id = uuid("training_id").references(Trainings.id)
    private val name = varchar("name", 50)
    private val tonnage = double("tonnage")
    private val intensity = double("intensity")
    private val count_of_lifting = integer("count_of_lifting")

    fun insert(dto: ExercisesDto) {
        transaction {
            insert {
                it[id] = dto.id
                it[training_id] = dto.training_id
                it[name] = dto.name
                it[tonnage] = dto.tonnage
                it[intensity] = intensity
                it[count_of_lifting] = count_of_lifting
            }
        }
    }
}