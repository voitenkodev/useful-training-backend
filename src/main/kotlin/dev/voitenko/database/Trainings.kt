package dev.voitenko.database

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

data class TrainingsDto(
    val id: UUID,
    val user_id: UUID,
    val duration: String,
    val date: String,
    val tonnage: Double,
    val countOfLifting: Int,
    val intensity: Double,
)

object Trainings : UUIDTable(name = "trainings") {

    private val user_id = uuid("user_id").references(Users.id)
    private val duration = varchar("duration", 50)
    private val date = varchar("date", 50)
    private val tonnage = double("tonnage")
    private val intensity = double("intensity")
    private val count_of_lifting = integer("count_of_lifting")

    fun insert(dto: TrainingsDto) {
        transaction {
            insert {
                it[id] = dto.id
                it[user_id] = dto.user_id
                it[duration] = dto.duration
                it[date] = dto.date
                it[tonnage] = dto.tonnage
                it[intensity] = intensity
                it[count_of_lifting] = count_of_lifting
            }
        }
    }
}