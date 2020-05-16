package me.agaman.ramona.api

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import me.agaman.ramona.model.StandupSaveRequest
import me.agaman.ramona.route.ApiRoutes

fun Route.apiRouter() {
    val standupController by lazy { StandupController() }

    post(ApiRoutes.STANDUP_SAVE.path) { request: StandupSaveRequest ->
        call.respond(standupController.standupSave(request))
    }

    get(ApiRoutes.STANDUP_GET.path) {
        val id = call.parameters["id"]!!.toInt()
        call.respond(standupController.standupGet(id))
    }

    get(ApiRoutes.STANDUP_LIST.path) {
        call.respond(standupController.standupList())
    }

    route("{...}") {
        handle {
            call.respond(HttpStatusCode.NotFound)
        }
    }
}
