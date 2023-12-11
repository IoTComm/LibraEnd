package com.kuro9.libraend.ws.fallback

import com.kuro9.libraend.db.DBHandler
import com.kuro9.libraend.db.type.DeskTable
import com.kuro9.libraend.router.config.ROOT_PATH
import com.kuro9.libraend.sse.SseController
import com.kuro9.libraend.ws.SeatInactiveTimerFactory
import com.kuro9.libraend.ws.WSHandler
import com.kuro9.libraend.ws.observer.DeskStateBroadcaster
import com.kuro9.libraend.ws.observer.SeatStateBroadcaster
import com.kuro9.libraend.ws.type.SeatError
import com.kuro9.libraend.ws.type.SeatListInputForm
import com.kuro9.libraend.ws.type.SeatListOutputForm
import com.kuro9.libraend.ws.type.SeatState
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*
import kotlin.collections.HashMap


@RestController
@RequestMapping(ROOT_PATH + "ws" + "/fallback")
class HttpPollFallBack(
    seatBroadcaster: SeatStateBroadcaster,
    deskBroadcaster: DeskStateBroadcaster,

    ) {

    init {
        seatBroadcaster.attach { stateChangedSeat[it.seatId] = it }
        deskBroadcaster.attach { stateChangedDesk[it.id] = it }
    }

    @Autowired
    lateinit var db: DBHandler

    @Autowired
    lateinit var adminConf: AdminConfigure

    @Autowired
    lateinit var timerFactory: SeatInactiveTimerFactory

    @Autowired
    lateinit var seatTimerMap: HashMap<Int, Timer>

    @Autowired
    lateinit var notifyHandler: SseController

    @Autowired
    lateinit var ws: WSHandler

    private val timeOutSeats: TreeSet<Int> = TreeSet()
    private val stateChangedDesk: HashMap<Int, DeskTable> = HashMap()
    private val stateChangedSeat: HashMap<Int, SeatState> = HashMap()


    @PutMapping("seats")
    fun updateSeats(
        @RequestBody body: SeatListInputForm,
        response: HttpServletResponse,
    ): SeatListOutputForm? {
        if (body.key != adminConf.key) {
            response.status = 403
            return null
        }

        val seatList = db.getSeatList().data!!
        val seatMap = java.util.HashMap<Int, Boolean>()
        for (seat in seatList) {
            seatMap[seat.seatId] = seat.isUsing
        }


        for (seat in body.seats) {
            if (seat.isActive) {
                seatTimerMap[seat.seatId]?.apply {
                    cancel()
                    purge()
                }
                seatTimerMap.remove(seat.seatId)
                continue
            }

            if (seat.seatId !in seatTimerMap && seatMap[seat.seatId]!! ) {
                // 처음 자리를 비운 상태 -> 타이머 시작

                val timer = timerFactory.getSeatTimer(seat.seatId) {
                    timeOutSeats.add(seat.seatId)
                    notifyHandler.notifyClientWithSeat(seat.seatId, "자리 비움으로 인해 퇴실처리 되었습니다. ", SseController.USER_WARN)
                    ws.broadcastToClient(
                        SeatError(
                            seat.seatId,
                            "${seat.seatId}번 자리 비움 상태. 자리를 정리하고 강제퇴실 처리해 주십시오. "
                        )
                    )
                }
                seatTimerMap[seat.seatId] = timer
            }
        }

        val returnData = SeatListOutputForm(
            seatStateChange = stateChangedSeat.map { it.value },
            seatTimeout = timeOutSeats.map {
                SeatError(
                    seatId = it,
                    description = "TimeOut"
                )
            },
            desk = stateChangedDesk.map { it.value }
        )
        timeOutSeats.clear()
        stateChangedSeat.clear()
        stateChangedDesk.clear()
        response.status = 200
        return returnData
    }

}