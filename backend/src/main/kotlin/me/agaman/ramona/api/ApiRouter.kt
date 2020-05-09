package me.agaman.ramona.api

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import me.agaman.ramona.model.*
import me.agaman.ramona.route.ApiRoutes

private val standupList: MutableSet<Standup> = mutableSetOf()

fun Route.apiRouter() {
    post(ApiRoutes.STANDUP_CREATE.path) {
        val request = call.receive<StandupCreateRequest>()
        val response = if (standupList.any { it.name == request.name }) {
            "An Standup already exists with the name '${request.name}'"
            StandupCreateResponse(null, "An Standup already exists with the name '${request.name}'")
        } else {
            val standup = Standup(
                id = standupList.size,
                name = request.name,
                startHour = request.startHour,
                finishHour = request.finishHour,
                days = request.days,
                questions = request.questions
            )
            standupList.add(standup)
            StandupCreateResponse(standup, null)
        }
        call.respond(response)
    }

    get(ApiRoutes.STANDUP_GET.path) {
        val standupId = call.parameters["id"]?.toInt()
        val response = standupList.firstOrNull { it.id == standupId }
            ?.let { StandupGetResponse(standup = it) }
            ?: StandupGetResponse(error = "Standup not found")
        call.respond(response)
    }

    get(ApiRoutes.STANDUP_LIST.path) {
        call.respond(StandupListResponse(standupList.sortedBy { it.id }))
    }

    route("{...}") {
        handle {
            call.respond(HttpStatusCode.NotFound)
        }
    }
}
