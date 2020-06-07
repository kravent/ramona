package me.agaman.ramona

import io.ktor.application.Application
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.contentType
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class MainRouterIntegrationTest {
    @ParameterizedTest
    @ValueSource(strings = ["/", "/standups", "/standups/main/", "/other-unknown-route"])
    fun `get main page`(uri: String) {
        withTestApplication(Application::module) {
            handleRequest(HttpMethod.Get, uri).apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(ContentType.Text.Html, response.contentType().withoutParameters())
                assertTrue(response.content?.contains("<title>Ramona</title>") ?: false)
            }
        }
    }
}
