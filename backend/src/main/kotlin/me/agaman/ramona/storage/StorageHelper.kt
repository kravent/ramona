package me.agaman.ramona.storage

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object StorageHelper {
    fun initDatabase() {
        Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1")
        transaction {
            SchemaUtils.create(StandupTable)
        }
    }
}
