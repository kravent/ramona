package me.agaman.ramona.di

import me.agaman.ramona.api.StandupController
import me.agaman.ramona.storage.StandupStorage
import me.agaman.ramona.storage.StorageHelper
import org.koin.dsl.module

val MainModule = module {
    single { StorageHelper() }
    single { StandupController(get()) }
    single { StandupStorage() }
}
