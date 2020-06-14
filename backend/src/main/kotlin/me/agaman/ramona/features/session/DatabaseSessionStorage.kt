package me.agaman.ramona.features.session

import io.ktor.util.toByteArray
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.writer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import me.agaman.ramona.storage.SessionStorage
import org.koin.core.KoinComponent
import org.koin.core.inject
import io.ktor.sessions.SessionStorage as KtorSessionStorage

class DatabaseSessionStorage : KtorSessionStorage, KoinComponent {
    private val sessionStorage: SessionStorage by inject()

    override suspend fun write(id: String, provider: suspend (ByteWriteChannel) -> Unit) {
        coroutineScope {
            writer(Dispatchers.Unconfined, autoFlush = true) { provider(channel) }.channel.toByteArray()
                .let { sessionStorage.save(id, it) }
        }
    }

    override suspend fun <R> read(id: String, consumer: suspend (ByteReadChannel) -> R): R =
        sessionStorage.get(id)?.let { data -> consumer(ByteReadChannel(data)) }
            ?: throw NoSuchElementException("Session $id not found")

    override suspend fun invalidate(id: String) {
        sessionStorage.delete(id)
    }
}
