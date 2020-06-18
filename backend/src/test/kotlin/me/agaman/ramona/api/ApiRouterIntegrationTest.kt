package me.agaman.ramona.api

import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import me.agaman.ramona.model.*
import me.agaman.ramona.test.RamonaIntegrationTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class ApiRouterIntegrationTest : RamonaIntegrationTest() {
    @ParameterizedTest
    @ValueSource(strings = ["GET", "POST"])
    fun `get route that doesn't exists`(method: HttpMethod) {
        withLoggedKtorApp {
            handleApiRequest(method, "/api/any/invalid/route").apply {
                assertEquals(HttpStatusCode.NotFound, response.status())
                assertNull(response.content)
            }
        }
    }

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
                assertEquals(StandupSaveResponse(error = "An Standup already exists with the name '${NEW_STANDUP.name}'"), response.getJsonContent())
            }
        }
    }

    @Test
    fun `get standup not found`() {
        withLoggedKtorApp {
            handleApiRequest(HttpMethod.Get, "/api/standup/get/1").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(StandupGetResponse(error = "Standup not found"), response.getJsonContent())
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
                assertEquals(StandupGetResponse(standup = savedStandup), response.getJsonContent())
            }
        }
    }

    @Test
    fun `get empty standups list`() {
        withLoggedKtorApp {
            handleApiRequest(HttpMethod.Get, "/api/standup/list").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(StandupListResponse(standups = emptyList()), response.getJsonContent())
            }
        }
    }

    @Test
    fun `save and get standups list`() {
        withLoggedKtorApp {
            val savedStandup = handleApiRequest(HttpMethod.Post, "/api/standup/save") {
                setJsonBody(StandupSaveRequest(NEW_STANDUP))
            }.response.getJsonContent<StandupSaveResponse>().standup!!

            handleApiRequest(HttpMethod.Get, "/api/standup/list").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(StandupListResponse(standups = listOf(savedStandup)), response.getJsonContent())
            }
        }
    }

    @Test
    fun `public get`() {
        withLoggedKtorApp {
            handleApiRequest(HttpMethod.Get, "/api/standup/public_get/any-external-id").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(StandupPublicGetResponse(error = "Standup not found"), response.getJsonContent())
            }
        }
    }

    @Test
    fun `save and do public get`() {
        withLoggedKtorApp {
            val savedStandup = handleApiRequest(HttpMethod.Post, "/api/standup/save") {
                setJsonBody(StandupSaveRequest(NEW_STANDUP))
            }.response.getJsonContent<StandupSaveResponse>().standup!!

            handleApiRequest(HttpMethod.Get, "/api/standup/public_get/${savedStandup.externalId}").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(StandupPublicGetResponse(standup = savedStandup), response.getJsonContent())
            }
        }
    }

    @Test
    fun `fill standup that is not found`() {
        withLoggedKtorApp {
            handleApiRequest(HttpMethod.Post, "/api/standup/fill") {
                setJsonBody(StandupFillRequest("any-external-id", mapOf(1 to "Any response")))
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(StandupFillResponse(error = "Standup not found"), response.getJsonContent())
            }
        }
    }

    @Test
    fun `save and fill standup`() {
        withLoggedKtorApp {
            val savedStandup = handleApiRequest(HttpMethod.Post, "/api/standup/save") {
                setJsonBody(StandupSaveRequest(NEW_STANDUP))
            }.response.getJsonContent<StandupSaveResponse>().standup!!

            handleApiRequest(HttpMethod.Post, "/api/standup/fill") {
                setJsonBody(StandupFillRequest(savedStandup.externalId, mapOf(1 to "Any response")))
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(StandupFillResponse(saved = true), response.getJsonContent())
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
