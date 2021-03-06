package me.agaman.ramona

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.UserIdPrincipal
import io.ktor.auth.form
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.sessions.Sessions
import me.agaman.ramona.di.MainModule
import me.agaman.ramona.features.*
import me.agaman.ramona.storage.StorageManager
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.inject

fun Application.module(skipKoinInstall: Boolean = false) {
    install(DefaultHeaders)
    install(CallLogging)
    install(ContentNegotiation) {
        apiSerialization()
    }
    install(Sessions) {
        apiSessionsCookie()
    }
    install(Csrf) {
        validateHeader("X-CSRF") { it.call.getCsrfToken() }
    }
    install(Authentication) {
        form {
            skipWhen { it.getCurrentUserOrNull() != null }
            challenge { call.respond(HttpStatusCode.Unauthorized) }
            validate {
                when (it.password) {
                    "testing" -> UserIdPrincipal(it.name)
                    else -> null
                }
            }
        }
    }
    if (!skipKoinInstall) {
        install(Koin) {
            modules(MainModule)
        }
    }

    val storageManager: StorageManager by inject()
    storageManager.initDatabase()

    install(Routing) {
        mainRouter()
    }
}
