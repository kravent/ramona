package me.agaman.ramona.storage

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction

open class StorageManager {
    protected open val databaseUrl = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"

    protected val tables: Array<Table> = arrayOf(
        StandupTable,
        StandupResponsesTable
    )

    fun initDatabase() {
        Database.connect(databaseUrl)
        transaction {
            SchemaUtils.create(*tables)
        }
    }
}
