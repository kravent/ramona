package me.agaman.ramona.model

import kotlinx.serialization.Serializable

@Serializable
data class UserCreateResponse(
    val users: UserListResponse,
    val error: String? = null
)
