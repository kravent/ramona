package me.agaman.ramona.model

import kotlinx.serialization.Serializable

@Serializable
data class StandupViewResponse(
    val standup: Standup? = null,
    val error: String? = null
)
