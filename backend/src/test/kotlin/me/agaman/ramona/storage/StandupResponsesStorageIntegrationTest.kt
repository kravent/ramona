package me.agaman.ramona.storage

import io.mockk.every
import io.mockk.mockk
import me.agaman.ramona.features.User
import me.agaman.ramona.model.Standup
import me.agaman.ramona.test.RamonaIntegrationTest
import org.junit.jupiter.api.Test
import org.koin.test.inject
import kotlin.test.assertEquals

internal class StandupResponsesStorageIntegrationTest : RamonaIntegrationTest() {
    private val storage: StandupResponsesStorage by inject()

    @Test
    fun `find empty`() {
        assertEquals(emptyList(), storage.find(mockkStandup(1)))
    }

    @Test
    fun `save and find`() {
        val standup1 = mockkStandup(1)
        val standup2 = mockkStandup(2)

        storage.save(standup1, User(USER1), RESPONSES1)
        storage.save(standup1, User(USER2), RESPONSES2)
        storage.save(standup2, User(USER1), RESPONSES3)

        assertEquals(listOf(USER1 to RESPONSES1, USER2 to RESPONSES2), storage.find(standup1))
        assertEquals(listOf(USER1 to RESPONSES3), storage.find(standup2))
    }

    private fun mockkStandup(standupId: Int): Standup = mockk {
        every { id } returns standupId
    }

    companion object {
        private const val USER1 = "user1"
        private const val USER2 = "user2"
        private val RESPONSES1 = mapOf(1 to "resp1", 2 to "resp2")
        private val RESPONSES2 = mapOf(1 to "other", 2 to "resp")
        private val RESPONSES3 = mapOf(1 to "more responses")
    }
}
