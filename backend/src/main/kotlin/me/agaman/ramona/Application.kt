package me.agaman.ramona

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.*
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.html.respondHtml
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.sessions.Sessions
import kotlinx.html.*
import me.agaman.ramona.api.apiRouter
import me.agaman.ramona.features.*
import me.agaman.ramona.route.Route
import me.agaman.ramona.storage.StorageHelper

fun Application.module() {
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
            skipWhen { it.getApiSession().currentUserName != null }
            challenge { call.respond(HttpStatusCode.Unauthorized) }
            validate {
                when (it.password) {
                    "testing" -> UserIdPrincipal(it.name)
                    else -> null
                }
            }
        }
    }

    StorageHelper.initDatabase()

    install(Routing) {
        authenticate {
            post(Route.LOGIN.path) {
                val currentUser = call.principal<UserIdPrincipal>() ?: error("No auth found")
                call.setApiSession(ApiSession(currentUserName = currentUser.name))
                call.respondText { call.getCsrfToken() }
            }

            post(Route.LOGOUT.path) {
                call.deleteApiSession()
                call.respondText { call.getCsrfToken() }
            }

            route(Route.API.path) {
                apiRouter()
            }
        }

        static(Route.STATIC.path) {
            resources("static")
        }

        get("{...}") {
            val csrfToken = call.getCsrfToken()
            call.respondHtml {
                head {
                    meta { charset = Charsets.UTF_8.name() }
                    link {
                        rel = LinkRel.stylesheet
                        href = "https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
                    }
                    title { +"Ramona" }
                }
                body {
                    hiddenInput {
                        id = "_csrf_token"
                        value = csrfToken
                    }
                    div { id = "root" }
                    script {
                        type = ScriptType.textJavaScript
                        src = "/static/app.js"
                    }
                }
            }
        }
    }
}
