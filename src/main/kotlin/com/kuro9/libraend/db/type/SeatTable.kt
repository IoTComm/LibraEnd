package com.kuro9.libraend.db.type

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class SeatTable(
    val seatId: Int,
    val isUsing: Boolean,
    val deskId: Int
)
