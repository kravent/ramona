package me.agaman.ramona.model

import kotlinx.serialization.Serializable

@Serializable
data class StandupFillRequest(
    val externalId: String,
    val responses: Map<Int, String>
)

@Serializable
data class StandupFillResponse(
    val saved: Boolean = false,
    val error: String? = null
)
