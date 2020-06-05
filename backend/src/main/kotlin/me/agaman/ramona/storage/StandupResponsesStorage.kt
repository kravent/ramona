package me.agaman.ramona.storage

import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import me.agaman.ramona.Serializer
import me.agaman.ramona.features.User
import me.agaman.ramona.model.Standup
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class StandupResponsesStorage {
    fun save(standup: Standup, currentUser: User, responses: Map<Int, String>) = transaction {
        StandupResponsesTable.insert {
            it[standupId] = standup.id
            it[user] = currentUser.name
            it[responsesJson] = Serializer.json.stringify(RESPONSES_SERIALIZER, responses)
        }
    }

    fun find(standup: Standup): List<Pair<String, Map<Int, String>>> = transaction {
        StandupResponsesTable.select { StandupResponsesTable.standupId eq standup.id }
            .map { it.toStandupResponsesPair() }
    }

    private fun ResultRow.toStandupResponsesPair(): Pair<String, Map<Int, String>> =
        this[StandupResponsesTable.user] to Serializer.json.parse(RESPONSES_SERIALIZER, this[StandupResponsesTable.responsesJson])

    companion object {
        private val RESPONSES_SERIALIZER = MapSerializer(Int.serializer(), String.serializer())
    }
}

object StandupResponsesTable : IntIdTable("standup_responses") {
    val standupId: Column<Int> = integer("standup_id").index()
    val user: Column<String> = varchar("user", 50)
    val responsesJson: Column<String> = text("responses_json")
}
