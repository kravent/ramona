package me.agaman.ramona.model

import kotlinx.serialization.Serializable

data class StandupGetResponseParams(val id: Int)

@Serializable
data class StandupGetResponse(
    val standup: Standup? = null,
    val error: String? = null
)
