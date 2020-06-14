package me.agaman.ramona.storage

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction

open class StorageManager {
    protected open val databaseUrl = "jdbc:h2:./build/development"
    private var isInitialized = false

    protected val tables: Array<Table> = arrayOf(
        SessionTable,
        StandupTable,
        StandupResponsesTable
    )

    open fun initDatabase() {
        if (!isInitialized) {
            Database.connect(databaseUrl)
            transaction {
                SchemaUtils.createMissingTablesAndColumns(*tables)
            }
            isInitialized = true
        }
    }
}
