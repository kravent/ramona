package me.agaman.ramona.model

import kotlinx.serialization.Serializable

data class StandupPublicGetRequest(val externalId: String)

@Serializable
data class StandupPublicGetResponse(
    val standup: Standup? = null,
    val error: String? = null
)
