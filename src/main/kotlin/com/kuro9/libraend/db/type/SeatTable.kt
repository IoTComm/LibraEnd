package com.kuro9.libraend.db.type

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class SeatTable(
    @Schema
    val seatId: Int,
    @Schema
    val isUsing: Boolean,
    @Schema
    val deskId: Int
)
