package me.agaman.ramona.model

import kotlinx.serialization.Serializable

@Serializable
data class StandupCreateRequest(
    val name: String,
    val startHour: Int,
    val finishHour: Int,
    val days: Set<WeekDay>,
    val questions: List<String>
)
