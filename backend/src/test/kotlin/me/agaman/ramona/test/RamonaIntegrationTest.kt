package me.agaman.ramona.test

import me.agaman.ramona.di.MainModule
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject

internal abstract class RamonaIntegrationTest : KoinTest {
    private val testStorageManager: TestStorageManager by inject()

    @BeforeEach
    fun `start koin`() {
        startKoin { modules(MainModule, TestModule) }
    }

    @AfterEach
    fun `stop koin`() {
        stopKoin()
    }

    @BeforeEach
    fun `init database`() {
        testStorageManager.initDatabase()
    }

    @AfterEach
    fun `clean database`() {
        testStorageManager.dropDatabase()
    }
}
