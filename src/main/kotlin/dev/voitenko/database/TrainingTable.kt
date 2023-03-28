package dev.voitenko.database

import dev.voitenko.services.trainings.dto.Exercise
import dev.voitenko.services.trainings.dto.ExerciseDate
import dev.voitenko.services.trainings.dto.Iteration
import dev.voitenko.services.trainings.dto.Training
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

data class TrainingsDto(
    val id: UUID,
    val user_id: UUID,
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
    val user_id = uuid("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
    val duration = varchar("duration", 50)
    val date = varchar("date", 50)
    val tonnage = double("tonnage")
    val intensity = double("intensity")
    val count_of_lifting = integer("count_of_lifting")
}

object Exercises : UUIDTable(name = "exercises") {
    val training_id = uuid("training_id").references(Trainings.id, onDelete = ReferenceOption.CASCADE)
    val name = varchar("name", 50)
    val tonnage = double("tonnage")
    val intensity = double("intensity")
    val count_of_lifting = integer("count_of_lifting")
}

object Iterations : UUIDTable(name = "iterations") {
    val exercise_id = uuid("exercise_id").references(Exercises.id, onDelete = ReferenceOption.CASCADE)
    val weight = double("weight")
    val repeat = integer("repeat")
}

fun Trainings.insert(
    userId: UUID,
    training: Training
) = transaction {

    val trainingId = training.id?.let { UUID.fromString(it) } ?: UUID.randomUUID()

    val resultId = insertAndGetId {
        it[user_id] = userId
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

    return@transaction resultId
}

fun Trainings.remove(
    trainingId: UUID
) = transaction {
    this@remove.deleteWhere {
        Trainings.id eq trainingId
    }
}

fun Trainings.getTrainings(
    select: Join.() -> Query
): List<Training> = transaction {
    this@getTrainings
        .leftJoin(Exercises)
        .leftJoin(Iterations)
        .select()
        .orderBy(Trainings.id, SortOrder.ASC)
        .groupBy(
            { p -> p.toTraining() }, { p -> p }
        ).map {
            val training = it.key
            val exercises = it.value.groupBy({ p -> p.toExercise() }, { p -> p.toIteration() })
            training to exercises
        }.map {
            val exercises: List<Exercise> = it.second.map { ex ->
                Exercise(
                    id = ex.key.id.toString(),
                    name = ex.key.name,
                    tonnage = ex.key.tonnage,
                    countOfLifting = ex.key.count_of_lifting,
                    intensity = ex.key.intensity,
                    iterations = ex.value.map { iteration ->
                        Iteration(
                            id = iteration.id.toString(),
                            weight = iteration.weight,
                            repeat = iteration.repeat
                        )
                    }
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


fun Trainings.getExercises(
    select: Join.() -> Query
): List<ExerciseDate> = transaction {
    this@getExercises
        .leftJoin(Exercises)
        .leftJoin(Iterations)
        .select()
        .orderBy(Trainings.id, SortOrder.ASC)
        .map {
            val training = it.toTraining()
            val exercise = it.toExercise()
            val iteration = it.toIteration()
            (training to exercise) to iteration
        }.groupBy(
            { it.first }, { it.second }
        ).map {
            val iterations = it.value.map { iteration ->
                Iteration(
                    id = iteration.id.toString(),
                    weight = iteration.weight,
                    repeat = iteration.repeat
                )
            }

            val exercise = Exercise(
                id = it.key.second.id.toString(),
                name = it.key.second.name,
                tonnage = it.key.second.tonnage,
                countOfLifting = it.key.second.count_of_lifting,
                intensity = it.key.second.intensity,
                iterations = iterations
            )

            ExerciseDate(
                trainingId = it.key.first.id.toString(),
                date = it.key.first.date,
                exercise = exercise
            )
        }
}

private fun ResultRow.toTraining(): TrainingsDto = TrainingsDto(
    id = this[Trainings.id].value,
    user_id = this[Trainings.user_id],
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