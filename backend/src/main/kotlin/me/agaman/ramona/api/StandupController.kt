package me.agaman.ramona.api

import me.agaman.ramona.model.StandupGetResponse
import me.agaman.ramona.model.StandupListResponse
import me.agaman.ramona.model.StandupSaveRequest
import me.agaman.ramona.model.StandupSaveResponse
import me.agaman.ramona.storage.StandupSaveResult
import me.agaman.ramona.storage.StandupStorage

class StandupController {
    private val standupStorage by lazy { StandupStorage() }

    fun standupSave(request: StandupSaveRequest): StandupSaveResponse =
        when (val saveResult = standupStorage.save(request.standup)) {
            is StandupSaveResult.StandupSaveResultOk ->
                StandupSaveResponse(standup = saveResult.standup)
            is StandupSaveResult.StandupSaveResultDuplicatedName ->
                StandupSaveResponse(error = "An Standup already exists with the name '${request.standup.name}'")
        }

    fun standupGet(standupId: Int): StandupGetResponse =
        standupStorage.get(standupId)
            ?.let { StandupGetResponse(standup = it) }
            ?: StandupGetResponse(error = "Standup not found")

    fun standupList(): StandupListResponse =
        StandupListResponse(standupStorage.getAll())
}
