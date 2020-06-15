package me.agaman.ramona

import io.ktor.client.request.forms.FormDataContent
import io.ktor.http.*
import io.ktor.server.testing.contentType
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import me.agaman.ramona.route.Route
import me.agaman.ramona.test.RamonaIntegrationTest
import me.agaman.ramona.test.TestCsrfTokenProvider
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class MainRouterIntegrationTest : RamonaIntegrationTest() {
    @ParameterizedTest
    @ValueSource(strings = ["/", "/standups", "/standups/main/", "/other-unknown-route"])
    fun `get main page`(uri: String) {
        withKtorApp {
            handleRequest(HttpMethod.Get, uri).apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(ContentType.Text.Html, response.contentType().withoutParameters())
                assertTrue(response.content?.contains("<title>Ramona</title>") ?: false)
            }
        }
    }

    @ParameterizedTest
    @CsvSource(value = [
        "invalid-csrf,                        testing,          403, ",
        "${TestCsrfTokenProvider.CSRF_TOKEN}, invalid_password, 401, ",
        "${TestCsrfTokenProvider.CSRF_TOKEN}, testing,          200, ${TestCsrfTokenProvider.CSRF_TOKEN}"
    ])
    fun `post login responses`(csrf: String, password: String, expectedHttpStatusCode: Int, expectedResponseBody: String?) {
        withKtorApp {
            handleRequest(HttpMethod.Post, Route.LOGIN.path) {
                addHeader("X-CSRF", csrf)
                addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
                setBody(
                    FormDataContent(
                        Parameters.build {
                            set("user", "any_user")
                            set("password", password)
                        }
                    ).bytes()
                )
            }.apply {
                assertEquals(HttpStatusCode.fromValue(expectedHttpStatusCode), response.status())
                assertEquals(expectedResponseBody, response.content)
            }
        }
    }

    @ParameterizedTest
    @CsvSource(value = [
        "invalid-csrf,                        403, ",
        "${TestCsrfTokenProvider.CSRF_TOKEN}, 200, ${TestCsrfTokenProvider.CSRF_TOKEN}"
    ])
    fun `post logout responses`(csrf: String, expectedHttpStatusCode: Int, expectedResponseBody: String?) {
        withLoggedKtorApp {
            handleRequest(HttpMethod.Post, Route.LOGOUT.path) {
                addHeader("X-CSRF", csrf)
            }.apply {
                assertEquals(HttpStatusCode.fromValue(expectedHttpStatusCode), response.status())
                assertEquals(expectedResponseBody, response.content)
            }
        }
    }
}
