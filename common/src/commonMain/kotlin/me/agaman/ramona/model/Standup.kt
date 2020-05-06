package me.agaman.ramona.model

import kotlinx.serialization.Serializable

@Serializable
data class Standup(
    val id: Int,
    val name: String,
    val startHour: Int,
    val finishHour: Int,
    val questions: List<String>
)
