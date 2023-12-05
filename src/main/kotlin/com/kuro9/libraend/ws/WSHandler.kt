package com.kuro9.libraend.ws

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.kuro9.libraend.db.DBHandler
import com.kuro9.libraend.router.config.COOKIE_SESS_KEY
import com.kuro9.libraend.ws.observer.DeskStateBroadcaster
import com.kuro9.libraend.ws.observer.SeatStateBroadcaster
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
class WSHandler(
    seatBroadcaster: SeatStateBroadcaster,
    deskBroadcaster: DeskStateBroadcaster,
) : TextWebSocketHandler() {
    private val masterNodes: MutableList<WebSocketSession> = mutableListOf()

    @Autowired
    lateinit var seatTimerMap: HashMap<Int, Timer>

    @Autowired
    lateinit var timerFactory: SeatInactiveTimerFactory

    @Autowired
    lateinit var db: DBHandler

    init {
        seatBroadcaster.attach { broadcastToClient(it) }
        deskBroadcaster.attach { broadcastToClient(it) }
    }

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
            if (seatState.seatId in seatTimerMap) return

            val timer = timerFactory.getSeatTimer(seatState.seatId) {
                broadcastToClient(
                    SeatError(
                        seatState.seatId,
                        "Seat Timeout without Logging Out!"
                    )
                )
            }
            seatTimerMap[seatState.seatId] = timer
        } else {
            seatTimerMap[seatState.seatId]?.apply {
                cancel()
                purge()
            }
            seatTimerMap.remove(seatState.seatId)
        }
    }

    fun getHttpSessionId(session: WebSocketSession): String {
        return session.attributes[COOKIE_SESS_KEY] as? String ?: ""
    }

    fun sendToClient(session: WebSocketSession, payload: String) {
        return session.sendMessage(TextMessage(payload))
    }

    fun broadcastToClient(payload: Any) {
        val jsonString = jacksonObjectMapper().writeValueAsString(payload)
        masterNodes.forEach {
            sendToClient(it, jsonString)
        }
    }

}