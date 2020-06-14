package me.agaman.ramona.features.session

import me.agaman.ramona.features.CsrfTokenProvider
import me.agaman.ramona.user.User

class ApiSessionProvider(
    private val csrfTokenProvider: CsrfTokenProvider
) {
    fun createSession(): ApiSession =
        ApiSession(
            csrfToken = csrfTokenProvider.generateRandomToken(),
            currentUser = null
        )

    fun createSession(currentUser: User): ApiSession =
        ApiSession(
            csrfToken = csrfTokenProvider.generateRandomToken(),
            currentUser = currentUser
        )
}
