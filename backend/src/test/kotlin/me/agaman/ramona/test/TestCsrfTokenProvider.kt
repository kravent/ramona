package me.agaman.ramona.test

import me.agaman.ramona.features.CsrfTokenProvider

internal class TestCsrfTokenProvider : CsrfTokenProvider {
    override fun generateRandomToken(): String = CSRF_TOKEN
    companion object {
        const val CSRF_TOKEN = "any-test-csrf-token"
    }
}
