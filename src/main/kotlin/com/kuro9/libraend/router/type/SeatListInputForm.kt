package com.kuro9.libraend.router.type

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class SeatListInputForm(
    val seatId: Int? = null,
    val isUsing: Boolean? = null,
    val deskId: Int? = null
)
