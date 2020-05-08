package me.agaman.ramona.api

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import me.agaman.ramona.model.Standup
import me.agaman.ramona.model.StandupCreateRequest
import me.agaman.ramona.model.StandupCreateResponse
import me.agaman.ramona.model.StandupViewResponse
import me.agaman.ramona.route.ApiRoute

private val standupList: MutableSet<Standup> = mutableSetOf()

fun Route.apiRouter() {
    post(ApiRoute.STANDUP_CREATE.path) {
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
                questions = request.questions
            )
            standupList.add(standup)
            StandupCreateResponse(standup, null)
        }
        call.respond(response)
    }

    get(ApiRoute.STANDUP_GET.path) {
        val standupId = call.parameters["id"]?.toInt()
        val response = standupList.firstOrNull { it.id == standupId }
            ?.let { StandupViewResponse(standup = it) }
            ?: StandupViewResponse(error = "Standup not found")
        call.respond(response)
    }

    route("{...}") {
        handle {
            call.respond(HttpStatusCode.NotFound)
        }
    }
}
