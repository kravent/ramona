package me.agaman.ramona.model

import kotlinx.serialization.Serializable

@Serializable
data class UserCreateRequest(
    val userName: String
)
