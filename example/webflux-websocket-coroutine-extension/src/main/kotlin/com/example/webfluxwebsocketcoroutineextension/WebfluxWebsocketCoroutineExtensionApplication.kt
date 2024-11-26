package com.example.webfluxwebsocketcoroutineextension

import app.boboc.annotation.WebSocketController
import app.boboc.annotation.WebSocketHandlerMapping
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.WebSocketSession

@SpringBootApplication
//@ComponentScan(basePackageClasses = [WebfluxWebsocketConfig::class])
class WebfluxWebsocketCoroutineExtensionApplication

fun main(args: Array<String>) {
    runApplication<WebfluxWebsocketCoroutineExtensionApplication>(*args)
}

data class MyMessage(val message: String)

@Configuration
class WebSocketConfiguration {
    @Bean
    fun handlerMapping(): HandlerMapping {
        return SimpleUrlHandlerMapping()
    }
}

@WebSocketController
class WebSocketController{

    @WebSocketHandlerMapping("message")
    suspend fun exampleWithJsonMessage(session: WebSocketSession, message: MyMessage): MyMessage {
        /**
         * Do Something
         * */

        return MyMessage(message = "Echo - ${message.message}")
    }

    @WebSocketHandlerMapping("message/own")
    suspend fun exampleWithOwnMessage(session: WebSocketSession, message: WebSocketMessage): String {
        /**
         * Do Something
         * */

        return "ECHO - ${message.payloadAsText}"
    }

}

