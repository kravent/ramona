package me.agaman.ramona.model

import kotlinx.serialization.Serializable

@Serializable
data class StandupSaveRequest(
    val standup: Standup
)

@Serializable
data class StandupSaveResponse(
    val standup: Standup? = null,
    val error: String? = null
)
