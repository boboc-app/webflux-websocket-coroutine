package app.bluelips.lib.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.reactor.mono
import org.springframework.web.reactive.socket.WebSocketSession

object WebSocketSessionUtil {
    private val objectMapper = ObjectMapper().registerKotlinModule()

    suspend fun WebSocketSession.sendMessage(message: Any?) {
        val session = this
        session.send(mono {
            session.textMessage(objectMapper.writeValueAsString(message ?: ""))
        }).awaitSingleOrNull()
    }
}
