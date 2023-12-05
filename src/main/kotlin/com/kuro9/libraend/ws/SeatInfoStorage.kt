package com.kuro9.libraend.ws

import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import java.util.*

@Component
class SeatInfoStorage {
    @get:Bean
    val seatTimerMap: HashMap<Int, Timer> = HashMap()
}