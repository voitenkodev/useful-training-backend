package dev.voitenko.database

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class UserDTO(
    val token: UUID,
    val email: String,
    val password: String
)

object Users : UUIDTable(name = "users") {

    private val email = Users.varchar("email", 30)
    private val password = Users.varchar("password", 25)

    fun insert(dto: UserDTO) {
        transaction {
            Users.insert {
                it[id] = dto.token
                it[email] = dto.email
                it[password] = dto.password
            }
        }
    }

    fun getByToken(token: UUID): UserDTO? {
        return transaction {
            val user = Users.select { Users.id.eq(token) }.firstOrNull()
            if (user != null) UserDTO(
                token = user[Users.id].value,
                email = user[email],
                password = user[password],
            ) else null
        }
    }

    fun getByEmail(email: String): UserDTO? {
        return transaction {
            val user = Users.select { Users.email.eq(email) }.firstOrNull()
            if (user != null) UserDTO(
                token = user[Users.id].value,
                email = user[Users.email],
                password = user[password],
            ) else null
        }
    }
}