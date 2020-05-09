package me.agaman.ramona.model

import kotlinx.serialization.Serializable

@Serializable
data class StandupListResponse(
    val standups: List<Standup>? = null,
    val error: String? = null
)
