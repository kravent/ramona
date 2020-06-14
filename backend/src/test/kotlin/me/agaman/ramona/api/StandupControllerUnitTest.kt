package me.agaman.ramona.api

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import me.agaman.ramona.user.User
import me.agaman.ramona.model.*
import me.agaman.ramona.storage.StandupResponsesStorage
import me.agaman.ramona.storage.StandupSaveResult
import me.agaman.ramona.storage.StandupStorage
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class StandupControllerUnitTest {
    private val standupStorage: StandupStorage = mockk(relaxed = true)
    private val standupResponsesStorage: StandupResponsesStorage = mockk(relaxed = true)

    private val controller = StandupController(
        standupStorage,
        standupResponsesStorage
    )

    @Test
    fun `save returns ok result`() {
        val requestStandup: Standup = mockk()
        val savedStandup: Standup = mockk()
        every { standupStorage.save(requestStandup) } returns StandupSaveResult.StandupSaveResultOk(savedStandup)

        val result = controller.save(StandupSaveRequest(requestStandup))

        assertEquals(StandupSaveResponse(standup = savedStandup), result)
        verify { standupStorage.save(requestStandup) }
    }

    @Test
    fun `save returns duplicated result`() {
        val requestStandup: Standup = mockk {
            every { name } returns STANDUP_NAME
        }
        every { standupStorage.save(requestStandup) } returns StandupSaveResult.StandupSaveResultDuplicatedName

        val result = controller.save(StandupSaveRequest(requestStandup))

        assertEquals(StandupSaveResponse(error = "An Standup already exists with the name '$STANDUP_NAME'"), result)
    }

    @Test
    fun `get returns standup`() {
        val standup: Standup = mockk()
        every { standupStorage.get(STANDUP_ID) } returns standup

        val result = controller.get(STANDUP_ID)

        assertEquals(StandupGetResponse(standup = standup), result)
    }

    @Test
    fun `get returns not found`() {
        every { standupStorage.get(STANDUP_ID) } returns null

        val result = controller.get(STANDUP_ID)

        assertEquals(StandupGetResponse(error = "Standup not found"), result)
    }

    @Test
    fun list() {
        val standup: Standup = mockk()
        every { standupStorage.getAll() } returns listOf(standup)

        val result = controller.list()

        assertEquals(StandupListResponse(listOf(standup)), result)
    }

    @Test
    fun `publicGet returns standup`() {
        val standup: Standup = mockk()
        every { standupStorage.getByExternalId(STANDUP_EXTERNAL_ID) } returns standup

        val result = controller.publicGet(STANDUP_EXTERNAL_ID)

        assertEquals(StandupPublicGetResponse(standup = standup), result)
    }

    @Test
    fun `publicGet not found`() {
        every { standupStorage.getByExternalId(STANDUP_EXTERNAL_ID) } returns null

        val result = controller.publicGet(STANDUP_EXTERNAL_ID)

        assertEquals(StandupPublicGetResponse(error = "Standup not found"), result)
    }

    @Test
    fun `fill doesn't finds standup`() {
        every { standupStorage.getByExternalId(STANDUP_EXTERNAL_ID) } returns null

        val result = controller.fill(StandupFillRequest(STANDUP_EXTERNAL_ID, RESPONSES), mockk())

        assertEquals(StandupFillResponse(error = "Standup not found"), result)
    }

    @Test
    fun `fill saves responses`() {
        val currentUser: User = mockk()
        val standup: Standup = mockk()
        every { standupStorage.getByExternalId(STANDUP_EXTERNAL_ID) } returns standup

        val result = controller.fill(StandupFillRequest(STANDUP_EXTERNAL_ID, RESPONSES), currentUser)

        assertEquals(StandupFillResponse(saved = true), result)
        verify { standupResponsesStorage.save(standup, currentUser, RESPONSES) }
    }

    companion object {
        private const val STANDUP_ID = 6
        private const val STANDUP_EXTERNAL_ID = "any-external-id"
        private const val STANDUP_NAME = "any_standup_name"
        private val RESPONSES = mapOf(1 to "any response")
    }
}
