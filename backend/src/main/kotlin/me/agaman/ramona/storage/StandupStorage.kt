package me.agaman.ramona.storage

import kotlinx.serialization.builtins.list
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.builtins.set
import me.agaman.ramona.Serializer
import me.agaman.ramona.model.Standup
import me.agaman.ramona.model.WeekDay
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class StandupStorage {
    fun save(standup: Standup): StandupSaveResult = transaction {
        when {
            StandupTable.select { (StandupTable.name eq standup.name) and (StandupTable.id neq standup.id) }.any() ->
                StandupSaveResult.StandupSaveResultDuplicatedName
            standup.id == 0 -> {
                val id = StandupTable.insertAndGetId {
                    it[name] = standup.name
                    it[startHour] = standup.startHour
                    it[finishHour] = standup.finishHour
                    it[daysJson] = Serializer.json.stringify(WeekDay.serializer().set, standup.days)
                    it[questionsJson] = Serializer.json.stringify(String.serializer().list, standup.questions)
                }
                StandupSaveResult.StandupSaveResultOk(standup.copy(id = id.value))
            }
            else -> {
                StandupTable.update({ StandupTable.id eq standup.id }) {
                    it[name] = standup.name
                    it[startHour] = standup.startHour
                    it[finishHour] = standup.finishHour
                    it[daysJson] = Serializer.json.stringify(WeekDay.serializer().set, standup.days)
                    it[questionsJson] = Serializer.json.stringify(String.serializer().list, standup.questions)
                }
                StandupSaveResult.StandupSaveResultOk(standup)
            }
        }
    }

    fun get(id: Int): Standup? = transaction {
        StandupTable.select { StandupTable.id eq id }
            .firstOrNull()
            ?.toStandup()
    }

    fun getAll(): List<Standup> = transaction {
        StandupTable.selectAll()
            .map { it.toStandup() }
    }

    private fun ResultRow.toStandup() = Standup(
        id = this[StandupTable.id].value,
        name = this[StandupTable.name],
        startHour = this[StandupTable.startHour],
        finishHour = this[StandupTable.finishHour],
        days = Serializer.json.parse(WeekDay.serializer().set, this[StandupTable.daysJson]),
        questions = Serializer.json.parse(String.serializer().list, this[StandupTable.questionsJson])
    )
}

sealed class StandupSaveResult {
    data class StandupSaveResultOk(val standup: Standup) : StandupSaveResult()
    object StandupSaveResultDuplicatedName : StandupSaveResult()
}

object StandupTable : IntIdTable("standup") {
    val name: Column<String> = varchar("name", 50).uniqueIndex()
    val startHour: Column<Int> = integer("start_hour")
    val finishHour: Column<Int> = integer("finish_hour")
    val daysJson: Column<String> = text("days_json")
    val questionsJson: Column<String> = text("questions_json")
}
