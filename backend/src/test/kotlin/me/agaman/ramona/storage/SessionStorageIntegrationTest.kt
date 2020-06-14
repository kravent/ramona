package me.agaman.ramona.storage

import me.agaman.ramona.test.RamonaIntegrationTest
import org.junit.jupiter.api.Test
import org.koin.test.inject
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class SessionStorageIntegrationTest : RamonaIntegrationTest() {
    private val storage: SessionStorage by inject()

    @Test
    fun `get not found`() {
        assertNull(storage.get(ID))
    }

    @Test
    fun `insert and get`() {
        storage.save(ID, DATA)

        assertEquals(DATA.toList(), storage.get(ID)?.toList())
    }

    @Test
    fun `insert, update and get`() {
        storage.save(ID, DATA)
        storage.save(ID, OTHER_DATA)

        assertEquals(OTHER_DATA.toList(), storage.get(ID)?.toList())
    }

    @Test
    fun `insert, delete and get`() {
        storage.save(ID, DATA)
        storage.delete(ID)

        assertNull(storage.get(ID))
    }

    @Test
    fun `insert multiple`() {
        storage.save(ID, DATA)
        storage.save(OTHER_ID, OTHER_DATA)

        assertEquals(DATA.toList(), storage.get(ID)?.toList())
        assertEquals(OTHER_DATA.toList(), storage.get(OTHER_ID)?.toList())
    }

    @Test
    fun `insert one and read another`() {
        storage.save(ID, DATA)

        assertNull(storage.get(OTHER_ID))
    }

    companion object {
        private const val ID = "any_id"
        private const val OTHER_ID = "any_other_id"
        private val DATA = byteArrayOf(4, 2, 6)
        private val OTHER_DATA = byteArrayOf(8, 22, 100)
    }
}
