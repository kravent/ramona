package me.agaman.ramona.helpers

import java.util.*

class UuidHelper {
    fun new(): UUID = UUID.randomUUID()
    fun toString(uuid: UUID): String = uuid.toString()
    fun fromString(uuid: String): UUID? = try {
        UUID.fromString(uuid)
    } catch (e: Exception) {
        null
    }
}
