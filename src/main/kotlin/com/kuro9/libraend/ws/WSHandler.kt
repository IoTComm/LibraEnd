package com.kuro9.libraend.ws

import com.kuro9.libraend.router.config.COOKIE_SESS_KEY
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler


@Component
class WSHandler : TextWebSocketHandler() {
    private val masterNodes: MutableList<WebSocketSession> = mutableListOf()

    @Throws(Exception::class)
    override fun afterConnectionEstablished(session: WebSocketSession) {

        val sessId = getHttpSessionId(session)

        //TODO DB call

        if (TODO("세션id가 관리자가 아니라면 스킵/관리자라면 masterNode에 등록하기")) {
            masterNodes.add(session)
        }
        else {
            session.close(CloseStatus(403))
            
        }
    }

    @Throws(Exception::class)
    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        masterNodes.remove(session)
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

    /**
     * @return null at parse failed, blank string at no sess_id key
     */
    fun getHttpSessionId(session: WebSocketSession): String? {
        return session.attributes[COOKIE_SESS_KEY] as? String
    }

    fun sendToClient(session: WebSocketSession, payload: String) {
        return session.sendMessage(TextMessage(payload))
    }
}