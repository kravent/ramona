package me.agaman.ramona.features.session

import kotlinx.serialization.Serializable
import me.agaman.ramona.user.User

@Serializable
data class ApiSession(
    val csrfToken: String,
    val currentUser: User?
)
