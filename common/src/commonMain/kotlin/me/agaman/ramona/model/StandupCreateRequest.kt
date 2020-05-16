package me.agaman.ramona.model

import kotlinx.serialization.Serializable

@Serializable
data class StandupCreateRequest(
    val standup: Standup
)
