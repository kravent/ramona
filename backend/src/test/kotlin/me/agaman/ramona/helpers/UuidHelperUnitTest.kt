package me.agaman.ramona.helpers

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNull

internal class UuidHelperUnitTest {
    private val helper = UuidHelper()

    @Test
    fun `two new uuids are different`() {
        assertNotEquals(helper.new(), helper.new())
    }

    @Test
    fun `create and transform twice`() {
        val uuid = helper.new()
        val result = helper.fromString(helper.toString(uuid))
        assertEquals(uuid, result)
    }

    @Test
    fun `parse invalid uuid string`() {
        assertNull(helper.fromString("invalid-uuid"))
    }
}
