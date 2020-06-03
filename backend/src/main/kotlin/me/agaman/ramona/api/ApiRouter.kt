package me.agaman.ramona.api

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import me.agaman.ramona.features.getCurrentUser
import me.agaman.ramona.model.StandupFillRequest
import me.agaman.ramona.model.StandupSaveRequest
import me.agaman.ramona.route.ApiRoutes
import org.koin.ktor.ext.inject

fun Route.apiRouter() {
    val standupController: StandupController by inject()

    post(ApiRoutes.STANDUP_SAVE.path) { request: StandupSaveRequest ->
        call.respond(standupController.save(request))
    }

    get(ApiRoutes.STANDUP_GET.path) {
        val id = call.parameters["id"]!!.toInt()
        call.respond(standupController.get(id))
    }

    get(ApiRoutes.STANDUP_LIST.path) {
        call.respond(standupController.list())
    }

    get(ApiRoutes.STANDUP_PUBLIC_GET.path) {
        val externalId = call.parameters["externalId"]!!
        call.respond(standupController.publicGet(externalId))
    }

    post(ApiRoutes.STANDUP_FILL.path) { request: StandupFillRequest ->
        call.respond(standupController.fill(request, call.getCurrentUser()))
    }

    route("{...}") {
        handle {
            call.respond(HttpStatusCode.NotFound)
        }
    }
}
