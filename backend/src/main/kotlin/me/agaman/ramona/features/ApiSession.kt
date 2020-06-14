package me.agaman.ramona.features

import io.ktor.application.ApplicationCall
import io.ktor.sessions.*
import me.agaman.ramona.features.session.*
import me.agaman.ramona.user.User
import org.koin.ktor.ext.get

private const val ONE_MONTH_IN_SECONDS: Long = 30 * 24 * 3600

fun Sessions.Configuration.apiSessionsCookie() {
    cookie<ApiSession>("sid", DatabaseSessionStorage()) {
        serializer = ApiSessionSerializer
        cookie.maxAgeInSeconds = ONE_MONTH_IN_SECONDS
    }
}

fun ApplicationCall.getApiSession(): ApiSession = sessions.getOrSet { this.application.get<ApiSessionProvider>().createSession() }
fun ApplicationCall.getCsrfToken(): String = getApiSession().csrfToken

fun ApplicationCall.setApiSession(session: ApiSession) = sessions.set(session)
fun ApplicationCall.updateApiSession(callback: (ApiSession) -> ApiSession) = sessions.set(callback(getApiSession()))
fun ApplicationCall.deleteApiSession() = sessions.clear<ApiSession>()
fun ApplicationCall.getCurrentUserOrNull(): User? = getApiSession().currentUser
fun ApplicationCall.getCurrentUser(): User = getCurrentUserOrNull() ?: error("User is not logged")

