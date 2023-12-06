package com.kuro9.libraend.sse

import com.kuro9.libraend.db.DBHandler
import com.kuro9.libraend.router.config.COOKIE_SESS_KEY
import com.kuro9.libraend.router.config.ROOT_PATH
import com.kuro9.libraend.ws.observer.SeatStateBroadcaster
import com.kuro9.libraend.ws.type.SeatState
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


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
    private val heartbeat: MutableSet<String> = mutableSetOf()
    private val executor = Executors.newScheduledThreadPool(1).apply {
        scheduleAtFixedRate({
            for (disconnected in emitters.keys - heartbeat) {
                emitters.remove(disconnected)
            }
            heartbeat.clear()
            emitters.forEach {
                it.value.send(SseEmitter.event().name(PING).data("ping"))
            }
        }, 300, 300, TimeUnit.SECONDS)
    }

    @GetMapping("/open-channel")
    fun attachClient(@CookieValue(COOKIE_SESS_KEY) sessId: String?): SseEmitter {
        if (sessId == null) throw ConnectionRejectedException("로그인 정보 없음")

        val emitter = SseEmitter(60 * 60 * 1000L)
        emitters[sessId] = emitter
        emitter.onTimeout { emitters.remove(sessId) }
        emitter.onCompletion { emitters.remove(sessId) }

        notifyClient(sessId, "hello, client")
        return emitter
    }

    @PutMapping("/heartbeat")
    fun keepAlive(@CookieValue(COOKIE_SESS_KEY) sessId: String): ResponseEntity<Unit> {
        return ResponseEntity.status(200).build()
    }

    fun notifyClient(sessId: String, payload: String, level: String = INFO) {
        // val message = SseMessage(0, payload)
        emitters[sessId]?.send(
            SseEmitter.event().name(level).data(payload)
        )
        // emitters[sessId]?.send(jacksonObjectMapper().writeValueAsString(message), MediaType.APPLICATION_JSON)
    }

    fun notifyClientWithSeat(seatId: Int, payload: String, level: String = INFO) {
        val sessId = db.getSessId(seatId)
        if (sessId == null) {
            println("sessid got null")
            return
        }
        notifyClient(sessId, payload, level)
    }

    fun broadcastSeatState(payload: SeatState) {
//        val message = SseMessage(1, payload)
//        emitters.forEach {
//            it.value.send(jacksonObjectMapper().writeValueAsString(message), MediaType.APPLICATION_JSON)
//        }

        for ((_, emitter) in emitters)
            emitter.send(SseEmitter.event().name(SEAT_STATE_CHANGE).data(payload))
    }

    @ExceptionHandler(ConnectionRejectedException::class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    fun handleConnectionRejectedException(ex: ConnectionRejectedException): String {
        return ex.localizedMessage
    }

    class ConnectionRejectedException(message: String = "No further information.") : RuntimeException(message)
    companion object {
        const val PING = "ping"
        const val INFO = "info"
        const val SEAT_STATE_CHANGE = "state_change"
        const val USER_WARN = "user_warn"
    }

}

