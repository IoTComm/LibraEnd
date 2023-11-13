package com.kuro9.libraend.router.type

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.sql.Timestamp

data class LoginInputForm(
    val id: Int,
    val pw: String
)

@JsonNaming(SnakeCaseStrategy::class)
data class ReservationInputForm(
    val seatId: Int,
    val startTime: Timestamp = Timestamp(System.currentTimeMillis() + 10000)
)
