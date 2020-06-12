package me.agaman.ramona

import io.ktor.client.request.forms.FormDataContent
import io.ktor.http.*
import io.ktor.server.testing.contentType
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import me.agaman.ramona.route.Route
import me.agaman.ramona.test.RamonaIntegrationTest
import me.agaman.ramona.test.TestCsrfTokenProvider
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import org.junit.jupiter.params.provider.ValueSource
import java.util.stream.Stream
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

    class PostLoginArgumentsProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of("invalid-csrf", "testing", HttpStatusCode.Forbidden, null),
            Arguments.of(TestCsrfTokenProvider.CSRF_TOKEN, "invalid_password", HttpStatusCode.Unauthorized, null),
            Arguments.of(TestCsrfTokenProvider.CSRF_TOKEN, "testing", HttpStatusCode.OK, TestCsrfTokenProvider.CSRF_TOKEN)
        )
    }

    @ParameterizedTest
    @ArgumentsSource(PostLoginArgumentsProvider::class)
    fun `post login`(csrf: String, password: String, expectedResponseStatus: HttpStatusCode, expectedResponseBody: String?) {
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
                assertEquals(expectedResponseStatus, response.status())
                assertEquals(expectedResponseBody, response.content)
            }
        }
    }
}
