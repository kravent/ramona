package me.agaman.ramona.storage

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.EntityIDFunctionProvider
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import org.jetbrains.exposed.sql.transactions.transaction

class SessionStorage {
    fun save(id: String, data: ByteArray) = transaction {
        when(SessionTable.select { SessionTable.id eq id }.any()) {
            true -> {
                SessionTable.update({ SessionTable.id eq id }) {
                    it[this.data] = ExposedBlob(data)
                }
            }
            false -> {
                SessionTable.insert {
                    it[this.id] = EntityIDFunctionProvider.createEntityID(id, SessionTable)
                    it[this.data] = ExposedBlob(data)
                }
            }
        }
    }

    fun get(id: String): ByteArray? = transaction {
        SessionTable.select { SessionTable.id eq id }
            .firstOrNull()
            ?.let { it[SessionTable.data].bytes }
    }

    fun delete(id: String) = transaction {
        SessionTable.deleteWhere { SessionTable.id eq id }
    }
}

object SessionTable : IdTable<String>("session") {
    override val id: Column<EntityID<String>> = varchar("id", 32).entityId()
    val data: Column<ExposedBlob> = blob("data")
    override val primaryKey by lazy { PrimaryKey(id) }
}
