package com.kuro9.libraend.sse

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.kuro9.libraend.db.DBHandler
import com.kuro9.libraend.router.config.COOKIE_SESS_KEY
import com.kuro9.libraend.router.config.ROOT_PATH
import com.kuro9.libraend.sse.type.Notify
import com.kuro9.libraend.sse.type.SseMessage
import com.kuro9.libraend.ws.observer.SeatStateBroadcaster
import com.kuro9.libraend.ws.type.SeatState
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.util.concurrent.ConcurrentHashMap


@RestController
@RequestMapping("$ROOT_PATH/noti")
class SseController(
    seatBroadcaster: SeatStateBroadcaster,
) {

    init {
        seatBroadcaster.attach { broadcastSeatState(it) }
    }

    @Autowired
    lateinit var db: DBHandler

    private val emitters: MutableMap<String, SseEmitter> = ConcurrentHashMap()

    @GetMapping("/open-channel")
    fun attachClient(@CookieValue(COOKIE_SESS_KEY) sessId: String?): SseEmitter {
        if (sessId == null) throw ConnectionRejectedException("로그인 정보 없음")

        val emitter = SseEmitter(60 * 60 * 1000L)
        emitters[sessId] = emitter
        emitter.onTimeout { emitters.remove(sessId) }
        emitter.onCompletion { emitters.remove(sessId) }

        notifyClient(sessId, Notify(0, "hello, client!"))
        return emitter
    }

    fun notifyClient(sessId: String, payload: Notify) {
        val message = SseMessage(0, payload)
        emitters[sessId]?.send(jacksonObjectMapper().writeValueAsString(message), MediaType.APPLICATION_JSON)
    }

    fun notifyClientWithSeat(seatId: Int, payload: Notify) {
        val sessId = db.getSessId(seatId)
        if (sessId == null) {
            println("sessid got null")
            return
        }
        notifyClient(sessId, payload)
    }

    fun broadcastSeatState(payload: SeatState) {
        val message = SseMessage(1, payload)
        emitters.forEach {
            it.value.send(jacksonObjectMapper().writeValueAsString(message), MediaType.APPLICATION_JSON)
        }
    }

    @ExceptionHandler(ConnectionRejectedException::class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    fun handleConnectionRejectedException(ex: ConnectionRejectedException): String {
        return ex.localizedMessage
    }


    class ConnectionRejectedException(message: String = "No further information.") : RuntimeException(message)

}

