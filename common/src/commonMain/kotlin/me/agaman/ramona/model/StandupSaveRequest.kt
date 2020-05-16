package me.agaman.ramona.model

import kotlinx.serialization.Serializable

@Serializable
data class StandupSaveRequest(
    val standup: Standup
)
