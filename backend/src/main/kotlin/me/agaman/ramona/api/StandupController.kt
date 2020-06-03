package me.agaman.ramona.api

import me.agaman.ramona.features.User
import me.agaman.ramona.model.*
import me.agaman.ramona.storage.StandupResponsesStorage
import me.agaman.ramona.storage.StandupSaveResult
import me.agaman.ramona.storage.StandupStorage

class StandupController(
    private val standupStorage: StandupStorage,
    private val standupResponsesStorage: StandupResponsesStorage
) {
    fun save(request: StandupSaveRequest): StandupSaveResponse =
        when (val saveResult = standupStorage.save(request.standup)) {
            is StandupSaveResult.StandupSaveResultOk ->
                StandupSaveResponse(standup = saveResult.standup)
            is StandupSaveResult.StandupSaveResultDuplicatedName ->
                StandupSaveResponse(error = "An Standup already exists with the name '${request.standup.name}'")
        }

    fun get(standupId: Int): StandupGetResponse =
        standupStorage.get(standupId)
            ?.let { StandupGetResponse(standup = it) }
            ?: StandupGetResponse(error = "Standup not found")

    fun list(): StandupListResponse =
        StandupListResponse(standupStorage.getAll())

    fun publicGet(externalId: String): StandupPublicGetResponse =
        standupStorage.getByExternalId(externalId)
            ?.let { StandupPublicGetResponse(standup = it) }
            ?: StandupPublicGetResponse(error = "Standup not found")

    fun fill(request: StandupFillRequest, currentUser: User): StandupFillResponse {
        val standup = standupStorage.getByExternalId(request.externalId)
            ?: return StandupFillResponse(error = "Standup not found")
        standupResponsesStorage.save(standup, currentUser, request.responses)
        return StandupFillResponse(saved = true)
    }
}
