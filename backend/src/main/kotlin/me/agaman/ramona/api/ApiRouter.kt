package me.agaman.ramona.api

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import me.agaman.ramona.model.StandupGetResponse
import me.agaman.ramona.model.StandupListResponse
import me.agaman.ramona.model.StandupSaveRequest
import me.agaman.ramona.model.StandupSaveResponse
import me.agaman.ramona.route.ApiRoutes
import me.agaman.ramona.storage.StandupSaveResult
import me.agaman.ramona.storage.StandupStorage

fun Route.apiRouter() {
    val standupStorage by lazy { StandupStorage() }

    post(ApiRoutes.STANDUP_SAVE.path) { request: StandupSaveRequest ->
        val response = when (val saveResult = standupStorage.save(request.standup)) {
            is StandupSaveResult.StandupSaveResultOk ->
                StandupSaveResponse(standup = saveResult.standup)
            is StandupSaveResult.StandupSaveResultDuplicatedName ->
                StandupSaveResponse(error = "An Standup already exists with the name '${request.standup.name}'")
        }
        call.respond(response)
    }

    get(ApiRoutes.STANDUP_GET.path) {
        val id = call.parameters["id"]!!.toInt()
        val response = standupStorage.get(id)
            ?.let { StandupGetResponse(standup = it) }
            ?: StandupGetResponse(error = "Standup not found")
        call.respond(response)
    }

    get(ApiRoutes.STANDUP_LIST.path) {
        call.respond(StandupListResponse(standupStorage.getAll()))
    }

    route("{...}") {
        handle {
            call.respond(HttpStatusCode.NotFound)
        }
    }
}
