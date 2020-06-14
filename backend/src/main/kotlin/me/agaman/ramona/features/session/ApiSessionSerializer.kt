package me.agaman.ramona.features.session

import io.ktor.sessions.SessionSerializer
import me.agaman.ramona.Serializer

object ApiSessionSerializer :
    SessionSerializer<ApiSession> {
    override fun deserialize(text: String): ApiSession = Serializer.json.parse(ApiSession.serializer(), text)
    override fun serialize(session: ApiSession): String = Serializer.json.stringify(ApiSession.serializer(), session)
}
