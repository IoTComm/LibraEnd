package com.kuro9.libraend.ws.type

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class TimeoutMessage(
    val seatId: Int,
    val message: String,
)
