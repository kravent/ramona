package me.agaman.ramona.test

import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.withTestApplication
import me.agaman.ramona.di.MainModule
import me.agaman.ramona.module
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

    fun <R> withKtorApp(test: TestApplicationEngine.() -> R): R = withTestApplication({ module(skipKoinInstall = true) }, test)
}
