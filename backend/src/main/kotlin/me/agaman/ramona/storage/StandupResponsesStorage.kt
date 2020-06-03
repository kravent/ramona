package me.agaman.ramona.storage

import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import me.agaman.ramona.Serializer
import me.agaman.ramona.features.User
import me.agaman.ramona.model.Standup
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

class StandupResponsesStorage {
    fun save(standup: Standup, currentUser: User, responses: Map<Int, String>) = transaction {
        StandupResponsesTable.insert {
            it[standupId] = standup.id
            it[user] = currentUser.name
            it[responsesJson] = Serializer.json.stringify(MapSerializer(Int.serializer(), String.serializer()), responses)
        }
    }
}

object StandupResponsesTable : IntIdTable("standup_responses") {
    val standupId: Column<Int> = integer("standup_id").index()
    val user: Column<String> = varchar("user", 50)
    val responsesJson: Column<String> = text("responses_json")
}
