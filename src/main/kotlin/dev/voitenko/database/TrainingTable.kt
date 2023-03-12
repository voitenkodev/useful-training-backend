package dev.voitenko.database

import dev.voitenko.services.trainings.dto.Training
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

data class TrainingsDto(
    val id: UUID,
//    val user_id: UUID,
    val duration: String,
    val date: String,
    val tonnage: Double,
    val count_of_lifting: Int,
    val intensity: Double,
)

data class ExercisesDto(
    val id: UUID,
    val training_id: UUID,
    val name: String,
    val tonnage: Double,
    val count_of_lifting: Int,
    val intensity: Double,
)

data class IterationsDto(
    val id: UUID,
    val exercise_id: UUID,
    val weight: Double,
    val repeat: Int,
)

object Trainings : UUIDTable(name = "trainings") {
    //    val user_id = uuid("user_id").references(Users.id)
    val duration = varchar("duration", 50)
    val date = varchar("date", 50)
    val tonnage = double("tonnage")
    val intensity = double("intensity")
    val count_of_lifting = integer("count_of_lifting")
}

object Exercises : UUIDTable(name = "exercises") {
    val training_id = uuid("training_id").references(Trainings.id)
    val name = varchar("name", 50)
    val tonnage = double("tonnage")
    val intensity = double("intensity")
    val count_of_lifting = integer("count_of_lifting")
}

object Iterations : UUIDTable(name = "iterations") {
    val exercise_id = uuid("exercise_id").references(Exercises.id)
    val weight = double("weight")
    val repeat = integer("repeat")
}

fun Trainings.insert(training: Training) = transaction {

    val trainingId = training.id?.let { UUID.fromString(it) } ?: UUID.randomUUID()

    insertAndGetId {
//        it[user_id] = dto.user_id
        it[id] = trainingId
        it[duration] = training.duration
        it[date] = training.date
        it[tonnage] = training.tonnage
        it[intensity] = training.intensity
        it[count_of_lifting] = training.countOfLifting
    }

    val exercises = training.exercises.map { it.copy(id = it.id ?: UUID.randomUUID().toString()) }

    Exercises.batchInsert(exercises) { ex ->
        this[Exercises.id] = UUID.fromString(ex.id)
        this[Exercises.training_id] = trainingId
        this[Exercises.name] = ex.name
        this[Exercises.tonnage] = ex.tonnage
        this[Exercises.intensity] = ex.intensity
        this[Exercises.count_of_lifting] = ex.countOfLifting
    }

    exercises.forEach { ex ->

        val iterations = ex.iterations.map { it.copy(id = it.id ?: UUID.randomUUID().toString()) }

        Iterations.batchInsert(iterations) { iter ->
            this[Iterations.id] = UUID.fromString(iter.id)
            this[Iterations.exercise_id] = UUID.fromString(ex.id)
            this[Iterations.weight] = iter.weight
            this[Iterations.repeat] = iter.repeat
        }
    }
}

fun Trainings.get(id: UUID): List<Training> = transaction {
    this@get
        .leftJoin(Exercises)
        .leftJoin(Iterations)
        .select {
            this@get.id eq id
        }.groupBy(
            { p -> p.toTraining() }, { p -> p }
        ).map {
            val training = it.key
            val exercises = it.value.groupBy({ p -> p.toExercise() }, { p -> p.toIteration() })

            training to exercises
        }.map {

            val exercises: List<Training.Exercise> = it.second.map { ex ->
                Training.Exercise(
                    id = ex.key.id.toString(),
                    name = ex.key.name,
                    iterations = ex.value.map { iteration ->
                        Training.Exercise.Iteration(
                            id = iteration.id.toString(),
                            weight = iteration.weight,
                            repeat = iteration.repeat
                        )
                    },
                    tonnage = ex.key.tonnage,
                    countOfLifting = ex.key.count_of_lifting,
                    intensity = ex.key.intensity
                )
            }

            Training(
                id = it.first.id.toString(),
                duration = it.first.duration,
                date = it.first.date,
                tonnage = it.first.tonnage,
                countOfLifting = it.first.count_of_lifting,
                intensity = it.first.intensity,
                exercises = exercises
            )
        }
}

private fun ResultRow.toTraining(): TrainingsDto = TrainingsDto(
    id = this[Trainings.id].value,
//    user_id = this[Trainings.user_id],
    duration = this[Trainings.duration],
    date = this[Trainings.date],
    tonnage = this[Trainings.tonnage],
    intensity = this[Trainings.intensity],
    count_of_lifting = this[Trainings.count_of_lifting],
)

private fun ResultRow.toExercise(): ExercisesDto = ExercisesDto(
    id = this[Exercises.id].value,
    training_id = this[Exercises.training_id],
    name = this[Exercises.name],
    tonnage = this[Exercises.tonnage],
    intensity = this[Exercises.intensity],
    count_of_lifting = this[Exercises.count_of_lifting],
)

private fun ResultRow.toIteration(): IterationsDto = IterationsDto(
    id = this[Iterations.id].value,
    exercise_id = this[Iterations.exercise_id],
    weight = this[Iterations.weight],
    repeat = this[Iterations.repeat],
)

//fun insert(dto: TrainingsDto) {
//    transaction {
//        insert {
//            it[id] = dto.id
//            it[user_id] = dto.user_id
//            it[duration] = dto.duration
//            it[date] = dto.date
//            it[tonnage] = dto.tonnage
//            it[intensity] = intensity
//            it[count_of_lifting] = count_of_lifting
//        }
//    }
//}
//
//fun getAll(user_id: UUID): List<TrainingsDto> = transaction {
//    Iterations
//        .select { Trainings.user_id eq user_id }
//        .map { it.toDto() }
//}
//
