package dev.voitenko.database

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class TokenDTO(
    val id: UUID,
    val email: String,
    val token: String
)

object Tokens : UUIDTable(name = "tokens") {

    private val email = Tokens.varchar("email", 25)
    private val token = Tokens.varchar("token", 50)

    fun insert(dto: TokenDTO) {
        transaction {
            Tokens.insert {
                it[id] = dto.id
                it[email] = dto.email
                it[token] = dto.token
            }
        }
    }
}