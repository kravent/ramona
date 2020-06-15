package me.agaman.ramona.test

import io.ktor.client.request.forms.FormDataContent
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.Parameters
import io.ktor.server.testing.*
import me.agaman.ramona.di.MainModule
import me.agaman.ramona.module
import me.agaman.ramona.route.Route
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal abstract class RamonaIntegrationTest : KoinTest {
    private val testStorageManager: TestStorageManager by inject()

    @BeforeAll
    fun `start koin`() {
        startKoin { modules(MainModule, TestModule) }
    }

    @AfterAll
    fun `stop koin`() {
        stopKoin()
    }

    @BeforeAll
    fun `init database`() {
        testStorageManager.initDatabase()
    }

    @BeforeEach
    fun `truncate database tables`() {
        testStorageManager.truncateTables()
    }

    fun withKtorApp(test: TestApplicationEngine.() -> Unit) = withTestApplication({ module(skipKoinInstall = true) }) {
        cookiesSession {
            test()
        }
    }

    fun withLoggedKtorApp(test: TestApplicationEngine.() -> Unit) = withKtorApp {
        handleRequest(HttpMethod.Post, Route.LOGIN.path) {
            addHeader("X-CSRF", TestCsrfTokenProvider.CSRF_TOKEN)
            addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
            setBody(
                FormDataContent(
                    Parameters.build {
                        set("user", "any_user")
                        set("password", "testing")
                    }
                ).bytes()
            )
        }
        test()
    }
}
