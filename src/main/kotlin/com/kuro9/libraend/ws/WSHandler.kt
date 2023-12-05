package com.kuro9.libraend.ws

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.kuro9.libraend.db.DBHandler
import com.kuro9.libraend.db.type.DeskTable
import com.kuro9.libraend.router.config.COOKIE_SESS_KEY
import com.kuro9.libraend.sse.SseController
import com.kuro9.libraend.sse.type.Notify
import com.kuro9.libraend.ws.observer.DeskStateBroadcaster
import com.kuro9.libraend.ws.observer.SeatStateBroadcaster
import com.kuro9.libraend.ws.type.SeatError
import com.kuro9.libraend.ws.type.SeatState
import com.kuro9.libraend.ws.type.WSMessage
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

    @Autowired
    lateinit var notifyHandler: SseController

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

        sendToClient(session, code)
        if (isAdmin) masterNodes.add(session)
        else {
            session.close(
                CloseStatus.POLICY_VIOLATION
            )
        }
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
                        "${seatState.seatId}번 자리 비움 상태. 자리를 정리하고 강제퇴실 처리해 주십시오. "
                    )
                )
                notifyHandler.notifyClientWithSeat(seatState.seatId, Notify(1, "자리 비움으로 인해 퇴실처리 되었습니다. "))
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

    fun sendToClient(session: WebSocketSession, payload: Any) {
        val data = when (payload) {
            is Int -> WSMessage(0, payload)
            is SeatState -> WSMessage(1, payload)
            is DeskTable -> WSMessage(2, payload)
            is SeatError -> WSMessage(3, payload)
            is String -> WSMessage(4, payload)
            else -> WSMessage(-1, null)
        }

        val jsonString = jacksonObjectMapper().writeValueAsString(data)
        return session.sendMessage(TextMessage(jsonString))
    }

    fun broadcastToClient(payload: Any) {
        masterNodes.forEach {
            sendToClient(it, payload)
        }
    }

    fun sendNotify(seatId: Int) {
        val sessId = db.getSessId(seatId) ?: return
        notifyHandler.notifyClient(sessId, Notify(1, "자리 비움으로 인해 퇴실처리 되었습니다. "))
    }

}