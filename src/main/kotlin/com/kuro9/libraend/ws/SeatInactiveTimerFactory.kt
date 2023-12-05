package com.kuro9.libraend.ws

import com.kuro9.libraend.ws.fallback.AdminConfigure
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class SeatInactiveTimerFactory {
    @Autowired
    lateinit var adminConfigure: AdminConfigure

    fun getSeatTimer(
        seatId: Int,
        description: String = "Timeout!",
        timeoutAction: () -> Unit,
    ): Timer {
        val timer = Timer()
        val timerTask = object : TimerTask() {
            override fun run() = timeoutAction()
        }
        timer.schedule(timerTask, adminConfigure.timeLimit)

        return timer
    }
}