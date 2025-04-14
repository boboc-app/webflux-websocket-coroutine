package app.boboc.handler

import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.mono
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono

abstract class CoroutineWebSocketHandler : WebSocketHandler {
    private fun receiveMessage(session: WebSocketSession): Mono<Void> {
        return mono {
            onOpen(session)
            session.receive().map {
                it.retain()
            }.asFlow()
                .onCompletion {
                    onClose(session)
                }
                .collect {
                    coroutineHandle(session, it)
                }
        }.then()
    }

    abstract suspend fun coroutineHandle(session: WebSocketSession, message: WebSocketMessage)

    open suspend fun onOpen(session: WebSocketSession) = Unit

    open suspend fun onClose(session: WebSocketSession) = Unit

    override fun handle(session: WebSocketSession): Mono<Void> {
        return receiveMessage(session)
    }
}
