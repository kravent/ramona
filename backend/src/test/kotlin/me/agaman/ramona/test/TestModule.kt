package me.agaman.ramona.test

import io.mockk.mockk
import me.agaman.ramona.features.CsrfTokenProvider
import me.agaman.ramona.storage.StorageManager
import org.koin.dsl.module

internal val TestModule = module {
    single { TestStorageManager() }
    single(override = true) { mockk<StorageManager>(relaxed = true) }
    single(override = true) { TestCsrfTokenProvider() as CsrfTokenProvider }
}
