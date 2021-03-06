package me.agaman.ramona.model

import kotlinx.serialization.Serializable

@Serializable
enum class WeekDay(val key: String) {
    Monday("Mo"), Tuesday("Tu"), Wednesday("We"), Thursday("Th"), Friday("Fr"), Saturday("Sa"), Sunday("Su")
}
