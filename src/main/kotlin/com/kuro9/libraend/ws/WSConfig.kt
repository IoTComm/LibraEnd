package com.kuro9.libraend.ws

import com.kuro9.libraend.router.config.ROOT_PATH
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry


@Configuration
@EnableWebSocket
class WSConfig : WebSocketConfigurer {

    @Autowired
    lateinit var webSocketHandler: WebSocketHandler

    @Autowired
    lateinit var interceptor: WSHandShakeInterceptor

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(webSocketHandler, ROOT_PATH + "ws")
            .setAllowedOrigins("*")
            .addInterceptors(interceptor)
    }


}