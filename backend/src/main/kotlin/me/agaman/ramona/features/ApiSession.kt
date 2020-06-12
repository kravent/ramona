package me.agaman.ramona.features

import io.ktor.application.ApplicationCall
import io.ktor.sessions.*
import org.koin.ktor.ext.get

private const val SEVEN_DAYS_IN_SECONDS: Long = 7 * 24 * 3600

fun Sessions.Configuration.apiSessionsCookie() {
    cookie<ApiSession>("session", SessionStorageMemory()) {
        cookie.maxAgeInSeconds = SEVEN_DAYS_IN_SECONDS
    }
}

data class User(
    val name: String
)

data class ApiSession(
    val csrfToken: String,
    val currentUserName: String?
)

class ApiSessionProvider(
    private val csrfTokenProvider: CsrfTokenProvider
) {
    fun createSession(): ApiSession = ApiSession(
        csrfToken = csrfTokenProvider.generateRandomToken(),
        currentUserName = null
    )

    fun createSession(currentUserName: String): ApiSession = ApiSession(
        csrfToken = csrfTokenProvider.generateRandomToken(),
        currentUserName = currentUserName
    )
}

fun ApplicationCall.getApiSession(): ApiSession = sessions.getOrSet { this.application.get<ApiSessionProvider>().createSession() }
fun ApplicationCall.getCsrfToken(): String = getApiSession().csrfToken

fun ApplicationCall.setApiSession(session: ApiSession) = sessions.set(session)
fun ApplicationCall.updateApiSession(callback: (ApiSession) -> ApiSession) = sessions.set(callback(getApiSession()))
fun ApplicationCall.deleteApiSession() = sessions.clear<ApiSession>()
fun ApplicationCall.getCurrentUserOrNull(): User? = getApiSession().currentUserName?.let { User(it) }
fun ApplicationCall.getCurrentUser(): User = getCurrentUserOrNull() ?: error("User is not logged")

