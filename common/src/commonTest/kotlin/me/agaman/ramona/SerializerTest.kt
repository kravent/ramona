package me.agaman.ramona

import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.KSerializer
import me.agaman.ramona.model.Standup
import me.agaman.ramona.model.WeekDay
import kotlin.test.Test
import kotlin.test.assertEquals

@ImplicitReflectionSerializer
internal class SerializerTest {
    @Test
    fun serializeAndDeserialize() {
        val result = serializeAndDeserialize(Standup.serializer(), STANDUP)

        assertEquals(STANDUP, result)
    }

    @Test
    fun serializeAndDeserializeWithAutomaticSerializer() {
        val result = serializeAndDeserialize(Serializer.serializerFor(STANDUP), STANDUP)

        assertEquals(STANDUP, result)
    }

    @Test
    fun serializeAndDeserializeListWithAutomaticSerializer() {
        val result = serializeAndDeserialize(Serializer.serializerFor(listOf(STANDUP)), listOf(STANDUP))

        assertEquals(listOf(STANDUP), result)
    }

    @Test
    fun serializeAndDeserializeSetWithAutomaticSerializer() {
        val result = serializeAndDeserialize(Serializer.serializerFor(setOf(STANDUP)), setOf(STANDUP))

        assertEquals(setOf(STANDUP), result)
    }

    @Test
    fun serializeAndDeserializeMapWithAutomaticSerializer() {
        val result = serializeAndDeserialize(Serializer.serializerFor(mapOf("key" to STANDUP)), mapOf("key" to STANDUP))

        assertEquals(mapOf("key" to STANDUP), result)
    }

    private fun <T>serializeAndDeserialize(serializer: KSerializer<T>, value: T): T =
        Serializer.json.stringify(serializer, value)
            .let { Serializer.json.parse(serializer, it) }

    companion object {
        val STANDUP = Standup(
            id = 33,
            externalId = "any-external-id",
            name = "any_name",
            startHour = 900,
            finishHour = 1100,
            days = setOf(WeekDay.Monday, WeekDay.Tuesday),
            questions = listOf("question 1", "question 2")
        )
    }
}
