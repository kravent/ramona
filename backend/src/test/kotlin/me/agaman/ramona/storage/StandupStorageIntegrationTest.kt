package me.agaman.ramona.storage

import me.agaman.ramona.model.Standup
import me.agaman.ramona.model.WeekDay
import me.agaman.ramona.test.RamonaIntegrationTest
import org.junit.jupiter.api.Test
import org.koin.test.inject
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class StandupStorageIntegrationTest : RamonaIntegrationTest() {
    private val storage: StandupStorage by inject()

    @Test
    fun `get non existent standup`() {
        assertNull(storage.get(1))
    }

    @Test
    fun `get non existent standup by external id`() {
        assertNull(storage.getByExternalId("any-external-id"))
    }

    @Test
    fun `create standup`() {
        val standupResult = storage.save(STANDUP)

        assertTrue(standupResult is StandupSaveResult.StandupSaveResultOk)
        assertTrue(standupResult.standup.id > 0)
        println(standupResult.standup.externalId)
        assertTrue(standupResult.standup.externalId.matches(Regex("^[a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12}$")))
        assertEquals(STANDUP.startHour, standupResult.standup.startHour)
        assertEquals(STANDUP.finishHour, standupResult.standup.finishHour)
        assertEquals(STANDUP.days, standupResult.standup.days)
        assertEquals(STANDUP.questions, standupResult.standup.questions)
    }

    @Test
    fun `create and get standup`() {
        val standup = storage.saveAssertingOkResult(STANDUP)

        assertEquals(standup, storage.get(standup.id))
        assertEquals(standup, storage.getByExternalId(standup.externalId))
    }

    @Test
    fun `create update and get standup`() {
        val standup = storage.saveAssertingOkResult(STANDUP)


        val updatedStandup = standup.copy(
            name = "other_name",
            startHour = 2000,
            finishHour = 2200,
            days = setOf(WeekDay.Sunday),
            questions = listOf("Other question")
        )
        val saveResult = storage.saveAssertingOkResult(updatedStandup)

        assertEquals(updatedStandup, saveResult)
        assertEquals(updatedStandup, storage.get(standup.id))
        assertEquals(updatedStandup, storage.getByExternalId(standup.externalId))
    }

    @Test
    fun `insert with repeated name`() {
        assertTrue(storage.save(STANDUP) is StandupSaveResult.StandupSaveResultOk)
        assertTrue(storage.save(STANDUP) is StandupSaveResult.StandupSaveResultDuplicatedName)
    }

    @Test
    fun `update with repeated name`() {
        val standup = storage.saveAssertingOkResult(STANDUP)
        assertTrue(storage.save(STANDUP.copy(name = "other_name")) is StandupSaveResult.StandupSaveResultOk)
        assertTrue(storage.save(standup.copy(name = "other_name")) is StandupSaveResult.StandupSaveResultDuplicatedName)
        assertEquals(standup, storage.get(standup.id))
    }

    @Test
    fun `get all`() {
        val standup1 = storage.saveAssertingOkResult(STANDUP)
        val standup2 = storage.saveAssertingOkResult(STANDUP.copy(name = "other_name"))

        assertEquals(listOf(standup1, standup2), storage.getAll())
    }

    private fun StandupStorage.saveAssertingOkResult(standup: Standup) =
        storage.save(standup)
            .let {
                assertTrue(it is StandupSaveResult.StandupSaveResultOk)
                it.standup
            }

    companion object {
        private val STANDUP = Standup(
            name = "any_name",
            startHour = 900,
            finishHour = 1100,
            days = setOf(WeekDay.Monday, WeekDay.Tuesday, WeekDay.Wednesday, WeekDay.Thursday, WeekDay.Friday),
            questions = listOf("Question 1", "Question 2")
        )
    }
}
