package com.kuro9.libraend.ws

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.kuro9.libraend.db.DBHandler
import com.kuro9.libraend.router.config.COOKIE_SESS_KEY
import com.kuro9.libraend.ws.type.SeatError
import com.kuro9.libraend.ws.type.SeatState
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.util.*


@Component
class WSHandler : TextWebSocketHandler() {
    private val masterNodes: MutableList<WebSocketSession> = mutableListOf()
    private val seatTimerMap: HashMap<Int, Timer> = HashMap()

    @Autowired
    lateinit var db: DBHandler

    @Throws(Exception::class)
    override fun afterConnectionEstablished(session: WebSocketSession) {

        val sessId = getHttpSessionId(session)

        val code = runCatching { db.isAdmin(sessId) }
            .getOrDefault(500)
        val isAdmin = (code == 200)

        if (isAdmin) masterNodes.add(session)
        else session.close(
            CloseStatus(
                code, when (code) {
                    401 -> "Please re-auth your accound and try again."
                    403 -> "You are not valid Admin!"
                    else -> "Internal Server Error"
                }
            )
        )
    }

    @Throws(Exception::class)
    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        masterNodes.remove(session)
    }

    @Throws(Exception::class)
    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val parsedMsg = String(message.asBytes())
        println("[WSS]: Got $parsedMsg from client!")
        val seatState = jacksonObjectMapper().readValue<SeatState>(parsedMsg)

        if (!seatState.isActive) {
            val timer = Timer()
            val timerTask = object : TimerTask() {
                override fun run() {
                    // TODO timeout
                    // 의자, 책상 state update
                    // admin call
                    broadcastToClient(
                        jacksonObjectMapper().writeValueAsString(
                            SeatError(
                                seatState.seatId,
                                "Seat Timeout without Logging Out!"
                            )
                        )
                    )
                    // 사용한 학생에게 알림
                }
            }
            timer.schedule(timerTask, 30 * 60 * 1000L)
            seatTimerMap[seatState.seatId] = timer
        }
        else {
            seatTimerMap[seatState.seatId]?.apply {
                cancel()
                purge()
            }
        }
    }

    fun getHttpSessionId(session: WebSocketSession): String {
        return session.attributes[COOKIE_SESS_KEY] as? String ?: ""
    }

    fun sendToClient(session: WebSocketSession, payload: String) {
        return session.sendMessage(TextMessage(payload))
    }

    fun broadcastToClient(payload: String) {
        masterNodes.forEach {
            sendToClient(it, payload)
        }
    }
}