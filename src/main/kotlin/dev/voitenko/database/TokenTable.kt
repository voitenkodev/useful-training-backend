package dev.voitenko.database

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class TokenDTO(
    val id: UUID,
    val email: String,
    val token: String
)

object TokenTable : UUIDTable(name = "tokens") {

    private val email = TokenTable.varchar("email", 25)
    private val token = TokenTable.varchar("token", 50)

    fun insert(dto: TokenDTO) = transaction {
        TokenTable.insert {
            it[id] = dto.id
            it[email] = dto.email
            it[token] = dto.token
        }
    }

    fun get(id: UUID) = transaction {
        TokenTable
            .select { this@TokenTable.id eq id }

            .map {
                TokenDTO(
                    id = it[this@TokenTable.id].value,
                    email = it[this@TokenTable.email],
                    token = it[this@TokenTable.token]
                )
            }
    }

    fun all() = transaction {
        TokenTable
            .selectAll()
            .map {
                TokenDTO(
                    id = it[this@TokenTable.id].value,
                    email = it[this@TokenTable.email],
                    token = it[this@TokenTable.token]
                )
            }
    }
}