package com.kuro9.libraend.router.type

import java.sql.Timestamp

data class ReservationReturnForm(
    val seatId: Int,
    val startTime: Timestamp = Timestamp(System.currentTimeMillis() + 10000)
)
