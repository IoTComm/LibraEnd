package com.kuro9.libraend.ws.observer

import com.kuro9.libraend.db.DBHandler
import com.kuro9.libraend.db.type.DeskTable
import com.kuro9.libraend.ws.type.SeatState
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class SeatStateBroadcaster {
    private val watchers = mutableSetOf<(SeatState) -> Unit>()

    @Autowired
    lateinit var db: DBHandler

    @Autowired
    lateinit var deskBroadcaster: DeskStateBroadcaster

    @OptIn(DelicateCoroutinesApi::class)
    fun updateState(seat: SeatState) {
        GlobalScope.async {
            watchers.forEach { it(seat) }
            val desk = db.updateDeskStatus(seat.seatId)
            deskBroadcaster.updateState(desk)
        }.start()
    }

    fun attach(onUpdate: (SeatState) -> Unit) = watchers.add(onUpdate)
    fun detach(onUpdate: (SeatState) -> Unit) = watchers.remove(onUpdate)
}

@Component
class DeskStateBroadcaster {
    private val watchers = mutableSetOf<(DeskTable) -> Unit>()

    @OptIn(DelicateCoroutinesApi::class)
    fun updateState(desk: DeskTable) {
        GlobalScope.async {
            watchers.forEach { it(desk) }
        }.start()
    }

    fun attach(onUpdate: (DeskTable) -> Unit) = watchers.add(onUpdate)
    fun detach(onUpdate: (DeskTable) -> Unit) = watchers.remove(onUpdate)
}