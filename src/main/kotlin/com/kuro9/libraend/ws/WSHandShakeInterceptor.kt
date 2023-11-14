package com.kuro9.libraend.ws

import com.kuro9.libraend.router.config.COOKIE_SESS_KEY
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import org.springframework.context.annotation.Configuration
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.http.server.ServletServerHttpRequest
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor
import org.springframework.web.util.WebUtils


@Configuration
class WSHandShakeInterceptor : HttpSessionHandshakeInterceptor() {
    override fun beforeHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        attributes: MutableMap<String, Any>
    ): Boolean {
        if (request is ServletServerHttpRequest) {
            val servletRequest: HttpServletRequest = request.servletRequest
            val token: Cookie? = WebUtils.getCookie(servletRequest, COOKIE_SESS_KEY)
            attributes[COOKIE_SESS_KEY] = token?.value ?: ""
        }
        else println(this::class.simpleName)

        return super.beforeHandshake(request, response, wsHandler, attributes)
    }
}