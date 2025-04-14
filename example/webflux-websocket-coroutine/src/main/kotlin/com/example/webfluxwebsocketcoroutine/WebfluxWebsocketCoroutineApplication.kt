package com.example.webfluxwebsocketcoroutine

import app.boboc.handler.CoroutineWebSocketHandler
import app.boboc.util.WebSocketSessionUtil.sendMessage
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession

@SpringBootApplication
class WebfluxWebsocketCoroutineApplication

fun main(args: Array<String>) {
    runApplication<WebfluxWebsocketCoroutineApplication>(*args)
}

class WebfluxCoroutineHandler : CoroutineWebSocketHandler(){
    override suspend fun coroutineHandle(session: WebSocketSession, message: WebSocketMessage) {
        session.sendMessage("ECHO ${message.payloadAsText}")
    }
}

@Configuration
class WebSocketConfiguration{
    @Bean
    fun handlerMapping() : HandlerMapping{
        return SimpleUrlHandlerMapping( mapOf("test" to WebfluxCoroutineHandler()), -1 )
    }
}
