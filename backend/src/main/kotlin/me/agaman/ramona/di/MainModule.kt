package me.agaman.ramona.di

import me.agaman.ramona.api.StandupController
import me.agaman.ramona.helpers.UuidHelper
import me.agaman.ramona.storage.StandupResponsesStorage
import me.agaman.ramona.storage.StandupStorage
import me.agaman.ramona.storage.StorageManager
import org.koin.dsl.module

val MainModule = module {
    single { StorageManager() }
    single { StandupController(get(), get()) }
    single { StandupStorage(get()) }
    single { StandupResponsesStorage() }
    single { UuidHelper() }
}
