# Webflux WebSocket Coroutine

## `webflux-websocket-coroutine`
It provides a suspend function between webSocket use of Webflux, the active module of Spring.
[websocket-demo](https://github.com/boboc-app/websocket-demo)
- `CoroutineWebSocketHandler`
  - CoroutineWebSocketHandler is an Abstract Class created by inheriting WebSocketHandler and provides the coroutineHandle function with suspend function.

- `WebSocketSession.sendMessage()`
  - This is an extension function that makes it possible to use as a suspend function in transmitting messages to client.

```
// Example
class WebfluxCoroutineHandler : CoroutineWebSocketHandler(){
    override suspend fun coroutineHandle(session: WebSocketSession, message: WebSocketMessage) {
        session.sendMessage("ECHO ${message.payloadAsText}")
    }
}
```

## `webflux-websocket-coroutine-extension`
This aids in the use of WebSocket communication in a concise form.
[websocket-ext-demo](https://github.com/boboc-app/websocket-ext-demo)
```
// Example

@WebSocketController
class WebSocketController{

    @WebSocketHandlerMapping("test001") // -> ws://localhost:8080/test001
    suspend fun exampleWithJsonMessage(session: WebSocketSession, message: MyMessage): MyMessage {
        // WebSocketMessage or WebSocketSession is optional argument
        /**
         * Do Something
         * */

        return MyMessage(message = "Echo - ${message.message}") // -> send message to client
    }

    @WebSocketHandlerMapping("test002") // -> ws://localhost:8080/test002
    suspend fun exampleWithOwnMessage(session: WebSocketSession, message: WebSocketMessage): String {
        /**
         * Do Something
         * */

        return "ECHO - ${message.payloadAsText}" // -> send message to client
    }

}
```