package com.kuro9.libraend.ws.fallback

import com.kuro9.libraend.db.DBHandler
import com.kuro9.libraend.db.type.DeskTable
import com.kuro9.libraend.router.config.ROOT_PATH
import com.kuro9.libraend.ws.SeatInactiveTimerFactory
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

        for (seat in body.seats) {
            if (seat.isActive) {
                seatTimerMap[seat.seatId]?.apply {
                    cancel()
                    purge()
                }
                seatTimerMap.remove(seat.seatId)
                continue
            }

            if (seat.seatId !in seatTimerMap) {
                // 처음 자리를 비운 상태 -> 타이머 시작
                val timer = timerFactory.getSeatTimer(seat.seatId) {
                    timeOutSeats.add(seat.seatId)
                    // TODO 휴대폰으로 알림?
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