package me.agaman.ramona.test

import org.koin.dsl.module

internal val TestModule = module {
    single { TestStorageManager() }
}
