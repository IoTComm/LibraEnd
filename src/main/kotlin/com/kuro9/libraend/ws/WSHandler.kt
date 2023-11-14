package com.kuro9.libraend.ws

import com.kuro9.libraend.router.config.COOKIE_SESS_KEY
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler


@Component
class WSHandler : TextWebSocketHandler() {
    private var masterNode: WebSocketSession? = null

    @Throws(Exception::class)
    override fun afterConnectionEstablished(session: WebSocketSession) {

        val sessId = session.attributes[COOKIE_SESS_KEY] as? String

        TODO("세션id가 관리자가 아니라면 스킵/관리자라면 masterNode에 등록하기")

        masterNode = session
    }

    @Throws(Exception::class)
    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        masterNode = null
    }

    @Throws(Exception::class)
    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
//        val id = session.id
//        CLIENTS.entries.forEach(Consumer<Map.Entry<String, WebSocketSession>> { (key, value): Map.Entry<String, WebSocketSession> ->
//            if (key != id) {  //같은 아이디가 아니면 메시지를 전달합니다.
//                try {
//                    value.sendMessage(message)
//                }
//                catch (e: IOException) {
//                    e.printStackTrace()
//                }
//            }
//        })
        TODO("메시지 수신 핸들링")
    }

    fun sendToClient(payload: String): Boolean {
        return masterNode?.let { it.sendMessage(TextMessage(payload));true } ?: false
    }
}