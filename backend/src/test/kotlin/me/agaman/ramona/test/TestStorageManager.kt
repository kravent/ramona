package me.agaman.ramona.test

import me.agaman.ramona.storage.StorageManager
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

internal class TestStorageManager : StorageManager() {
    override val databaseUrl = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"
    fun dropDatabase() {
        transaction {
            SchemaUtils.drop(*tables)
        }
    }
}
