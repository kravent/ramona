package me.agaman.ramona.model

import kotlinx.serialization.Serializable

@Serializable
data class StandupCreateRequest(
    val name: String,
    val startHour: Int,
    val finishHour: Int,
    val questions: List<String>
)
