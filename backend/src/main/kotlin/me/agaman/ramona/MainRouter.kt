package me.agaman.ramona

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.auth.UserIdPrincipal
import io.ktor.auth.authenticate
import io.ktor.auth.principal
import io.ktor.html.respondHtml
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import kotlinx.html.*
import me.agaman.ramona.api.apiRouter
import me.agaman.ramona.features.deleteApiSession
import me.agaman.ramona.features.getCsrfToken
import me.agaman.ramona.features.session.ApiSessionProvider
import me.agaman.ramona.features.setApiSession
import me.agaman.ramona.route.Route
import me.agaman.ramona.user.User
import org.koin.ktor.ext.inject

fun Routing.mainRouter() {
    val apiSessionProvider: ApiSessionProvider by inject()

    authenticate {
        post(Route.LOGIN.path) {
            val currentUser = call.principal<UserIdPrincipal>() ?: error("No auth found")
            call.setApiSession(apiSessionProvider.createSession(User(currentUser.name)))
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
        call.respondMainPage(call.getCsrfToken())
    }
}

private suspend fun ApplicationCall.respondMainPage(csrfToken: String) = respondHtml {
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
