package me.agaman.ramona.api

import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import me.agaman.ramona.model.*
import me.agaman.ramona.test.RamonaIntegrationTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class ApiRouterIntegrationTest : RamonaIntegrationTest() {
    @Test
    fun `save standup ok`() {
        withLoggedKtorApp {
            handleApiRequest(HttpMethod.Post, "/api/standup/save") {
                setJsonBody(StandupSaveRequest(NEW_STANDUP))
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                response.getJsonContent<StandupSaveResponse>().also { saveResponse ->
                    assertNull(saveResponse.error)
                    saveResponse.standup.let { standup ->
                        assertNotNull(standup)
                        assertTrue(standup.id > 0)
                        assertTrue(standup.externalId.isNotBlank())
                        assertEquals(NEW_STANDUP.name, standup.name)
                        assertEquals(NEW_STANDUP.startHour, standup.startHour)
                        assertEquals(NEW_STANDUP.finishHour, standup.finishHour)
                        assertEquals(NEW_STANDUP.days, standup.days)
                        assertEquals(NEW_STANDUP.questions, standup.questions)
                    }
                }
            }
        }
    }

    @Test
    fun `save standup with a name that already exists`() {
        withLoggedKtorApp {
            handleApiRequest(HttpMethod.Post, "/api/standup/save") {
                setJsonBody(StandupSaveRequest(NEW_STANDUP))
            }

            handleApiRequest(HttpMethod.Post, "/api/standup/save") {
                setJsonBody(StandupSaveRequest(NEW_STANDUP.copy(startHour = 200)))
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                response.getJsonContent<StandupSaveResponse>().also { saveResponse ->
                    assertNull(saveResponse.standup)
                    assertEquals("An Standup already exists with the name '${NEW_STANDUP.name}'", saveResponse.error)
                }
            }
        }
    }

    @Test
    fun `get standup not found`() {
        withLoggedKtorApp {
            handleApiRequest(HttpMethod.Get, "/api/standup/get/1").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                response.getJsonContent<StandupGetResponse>().let { getResponse ->
                    assertNull(getResponse.standup)
                    assertEquals("Standup not found", getResponse.error)
                }
            }
        }
    }

    @Test
    fun `save and get standup`() {
        withLoggedKtorApp {
            val savedStandup = handleApiRequest(HttpMethod.Post, "/api/standup/save") {
                setJsonBody(StandupSaveRequest(NEW_STANDUP))
            }.response.getJsonContent<StandupSaveResponse>().standup!!

            handleApiRequest(HttpMethod.Get, "/api/standup/get/${savedStandup.id}").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                response.getJsonContent<StandupGetResponse>().let { getResponse ->
                    assertNull(getResponse.error)
                    assertEquals(savedStandup, getResponse.standup)
                }
            }
        }
    }

    companion object {
        private val NEW_STANDUP = Standup(
            name = "any_name",
            startHour = 900,
            finishHour = 1100,
            days = setOf(WeekDay.Monday, WeekDay.Tuesday),
            questions = listOf("question 1", "question 2")
        )
    }
}
